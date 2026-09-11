/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.runtime.xml.axis;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnsupportedFeatureException;
import org.rumbledb.expressions.xml.axis.ForwardStepExpr;
import org.rumbledb.expressions.xml.axis.ReverseStepExpr;
import org.rumbledb.runtime.xml.axis.forward.AttributeAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.ChildAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.DescendantAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.DescendantOrSelfAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.FollowingAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.FollowingSiblingAxisIterator;
import org.rumbledb.runtime.xml.axis.forward.SelfAxisIterator;
import org.rumbledb.runtime.xml.axis.reverse.AncestorAxisIterator;
import org.rumbledb.runtime.xml.axis.reverse.AncestorOrSelfAxisIterator;
import org.rumbledb.runtime.xml.axis.reverse.ParentAxisIterator;
import org.rumbledb.runtime.xml.axis.reverse.PrecedingAxisIterator;
import org.rumbledb.runtime.xml.axis.reverse.PrecedingSiblingAxisIterator;

public class AxisIteratorVisitor {

    public AxisIterator visit(ForwardStepExpr forwardStep, RuntimeStaticContext staticContext) {
        switch (forwardStep.getForwardAxis()) {
            case SELF:
                return new SelfAxisIterator(staticContext);
            case CHILD:
                return new ChildAxisIterator(staticContext);
            case ATTRIBUTE:
                return new AttributeAxisIterator(staticContext);
            case FOLLOWING:
                return new FollowingAxisIterator(staticContext);
            case DESCENDANT:
                return new DescendantAxisIterator(staticContext);
            case FOLLOWING_SIBLING:
                return new FollowingSiblingAxisIterator(staticContext);
            case DESCENDANT_OR_SELF:
                return new DescendantOrSelfAxisIterator(staticContext);
            default:
                throw new UnsupportedFeatureException(
                        "Axis " + forwardStep.getForwardAxis() + "unrecognized", ExceptionMetadata.EMPTY_METADATA);
        }
    }

    public AxisIterator visit(ReverseStepExpr reverseStep, RuntimeStaticContext staticContext) {
        switch (reverseStep.getReverseAxis()) {
            case PARENT:
                return new ParentAxisIterator(staticContext);
            case ANCESTOR:
                return new AncestorAxisIterator(staticContext);
            case PRECEDING:
                return new PrecedingAxisIterator(staticContext);
            case ANCESTOR_OR_SELF:
                return new AncestorOrSelfAxisIterator(staticContext);
            case PRECEDING_SIBLING:
                return new PrecedingSiblingAxisIterator(staticContext);
            default:
                throw new UnsupportedFeatureException(
                        "Axis " + reverseStep.getReverseAxis() + "unrecognized", ExceptionMetadata.EMPTY_METADATA);
        }
    }
}
