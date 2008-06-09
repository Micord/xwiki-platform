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

import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.parser.Syntax;

/**
 * Performs a transformation on a Document's list of {@link org.xwiki.rendering.block.Block}.
 * This used for example for transforming Macro Blocks into other Blocks corresponding to the execution of the Macros.
 * Another example of transformation would be looking for all words that have an entry on Wikipedia and adding links
 * to them.
 * 
 * @version $Id$
 * @since 1.5M2
 */
public interface Transformation extends Comparable<Transformation>
{
    /**
     * This component's role, used when code needs to look it up.
     */
    String ROLE = Transformation.class.getName();

    int getPriority();

    /**
     * Look for all Macro Blocks in the passed DOM, find the corresponding macros (using the passed Syntax since
     * Macro components are registered against a given Syntax), execute them and replace the Macro Blocks with the
     * Blocks generated by the Macro executions.
     *
     * @param dom the AST representing the page in Blocks
     * @param syntax the Syntax for which to do the transformation. This is required since Macro components are
     *        registered against syntaxes. For example "xwiki/2.0", "confluence/1.0", etc.
     * @throws TransformationException if the transformation fails for any reason
     */
    void transform(XDOM dom, Syntax syntax)
        throws TransformationException;
}
