/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.transformation;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import org.xwiki.rendering.scaffolding.AbstractRenderingTestCase;
import org.xwiki.rendering.scaffolding.TestEventsListener;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.block.MacroBlock;
import org.xwiki.rendering.parser.Syntax;
import org.xwiki.rendering.parser.SyntaxType;

public class MacroTransformationTest extends AbstractRenderingTestCase
{
    private MacroTransformation transformation;
    
    protected void setUp() throws Exception
    {
        super.setUp();

        this.transformation = new MacroTransformation();
        this.transformation.compose(getComponentManager());
    }

    /**
     * Test that a simple macro is correctly evaluated.
     */
    public void testSimpleMacroTransform() throws Exception
    {
        String expected = "beginMacroMarker: [testsimplemacro] [] [null]\n"
            + "beginParagraph\n"
            + "onWord: [simplemacro0]\n"
            + "endParagraph\n"
            + "endMacroMarker: [testsimplemacro] [] [null]\n";

        XDOM dom = new XDOM(Arrays.asList((Block) new MacroBlock("testsimplemacro",
            Collections.<String, String>emptyMap())));
        
        this.transformation.transform(dom, new Syntax(SyntaxType.XWIKI, "2.0"));

        StringWriter sw = new StringWriter();
        dom.traverse(new TestEventsListener(sw));
        assertEquals(expected, sw.toString());
    }

    /**
     * Test that a macro can generate another macro.
     */
    public void testNestedMacroTransform() throws Exception
    {
        String expected = "beginMacroMarker: [testnestedmacro] [] [null]\n"
            + "beginParagraph\n"
            + "onWord: [simplemacro0]\n"
            + "endParagraph\n"
            + "endMacroMarker: [testnestedmacro] [] [null]\n";

        XDOM dom = new XDOM(Arrays.asList((Block) new MacroBlock("testnestedmacro",
            Collections.<String, String>emptyMap())));
    
        this.transformation.transform(dom, new Syntax(SyntaxType.XWIKI, "2.0"));

        StringWriter sw = new StringWriter();
        dom.traverse(new TestEventsListener(sw));
        assertEquals(expected, sw.toString());
    }
    
    /**
     * Test that we have a safeguard against infinite recursive macros.
     */
    public void testInfiniteRecursionMacroTransform() throws Exception
    {
        String expected = "beginMacroMarker: [testrecursivemacro] [] [null]\n"
            + "onMacro: [testrecursivemacro] [] [null]\n"
            + "endMacroMarker: [testrecursivemacro] [] [null]\n";
        
        XDOM dom = new XDOM(Arrays.asList((Block) new MacroBlock("testrecursivemacro",
            Collections.<String, String>emptyMap())));
    
        this.transformation.transform(dom, new Syntax(SyntaxType.XWIKI, "2.0"));

        StringWriter sw = new StringWriter();
        dom.traverse(new TestEventsListener(sw));
        assertEquals(expected, sw.toString());
    }
    
    /**
     * Test that macro priorities are working.
     */
    public void testPrioritiesMacroTransform() throws Exception
    {
        String expected = "beginMacroMarker: [testsimplemacro] [] [null]\n"
            + "beginParagraph\n"
            + "onWord: [simplemacro1]\n"
            + "endParagraph\n"
            + "endMacroMarker: [testsimplemacro] [] [null]\n"
            + "beginMacroMarker: [testprioritymacro] [] [null]\n"
            + "beginParagraph\n"
            + "onWord: [word]\n"
            + "endParagraph\n"
            + "endMacroMarker: [testprioritymacro] [] [null]\n";

        XDOM dom = new XDOM(Arrays.asList(
            (Block) new MacroBlock("testsimplemacro", Collections.<String, String>emptyMap()),
            (Block) new MacroBlock("testprioritymacro", Collections.<String, String>emptyMap())));

        this.transformation.transform(dom, new Syntax(SyntaxType.XWIKI, "2.0"));

        StringWriter sw = new StringWriter();
        dom.traverse(new TestEventsListener(sw));
        assertEquals(expected, sw.toString());
    }
    
}
