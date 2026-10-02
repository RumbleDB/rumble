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
package org.rumbledb.expressions.xml.axis;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.StepExpr;
import org.rumbledb.expressions.xml.node_test.NodeTest;
import org.rumbledb.runtime.xml.axis.AxisIterator;
import org.rumbledb.runtime.xml.axis.AxisIteratorVisitor;

public class ReverseStepExpr extends StepExpr {
    @Getter
    private final ReverseAxis reverseAxis;

    private final NodeTest nodeTest;

    public ReverseStepExpr(ReverseAxis reverseAxis, NodeTest nodeTest, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.reverseAxis = reverseAxis;
        this.nodeTest = nodeTest;
    }

    @Override
    public List<Node> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.reverseAxis.getAxisValue());
        sb.append(this.nodeTest.toString());
    }

    @Override
    public AxisIterator accept(AxisIteratorVisitor visitor, RuntimeStaticContext staticContext) {
        return visitor.visit(this, staticContext);
    }

    @Override
    public NodeTest getNodeTest() {
        return this.nodeTest;
    }
}
