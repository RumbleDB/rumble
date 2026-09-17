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
package org.rumbledb.compiler.backend;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

/** Builds runtime plans and initializes their dynamic contexts. */
@Log4j2
public final class RuntimePlanBuilder {
    private RuntimePlanBuilder() {}

    public static ItemRuntimePlan generateRuntimeIterator(Node node, RumbleConfiguration conf) {
        ItemRuntimePlan result = new RuntimeIteratorVisitor(conf).visit(node, null);
        if (conf.debug().printIteratorTree() || conf.debug().logging()) {
            StringBuilder sb = new StringBuilder();
            result.print(sb, 0);
            log.debug(sb);
        }
        return result;
    }

    public static RumbleConfiguration getEffectiveConfiguration(
            Node node, RumbleConfiguration.RumbleConfigurationBuilder builder) {
        return new EffectiveConfigurationVisitor().getEffectiveConfiguration(node, builder);
    }

    public static DynamicContext createDynamicContext(Node node, RumbleConfiguration configuration) {
        return createDynamicContext(node, configuration, ExternalBindings.empty());
    }

    public static DynamicContext createDynamicContext(
            Node node, RumbleConfiguration configuration, ExternalBindings externalBindings) {
        DynamicContextVisitor visitor = new DynamicContextVisitor(configuration, externalBindings);
        return visitor.visit(node, null);
    }
}
