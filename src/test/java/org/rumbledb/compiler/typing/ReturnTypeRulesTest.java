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
package org.rumbledb.compiler.typing;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.types.SequenceType;

public class ReturnTypeRulesTest {

    private static final File DELTA_TABLE =
            new File(System.getProperty("user.dir"), "src/test/resources/queries/sample_json_delta");

    @Test
    public void unreadableFileSchemaLeavesTheTypeUnknown() {
        Optional<SequenceType> type = ReturnTypeRules.fileSchemaType(deltaFileCall(), context(), path -> {
            throw new IllegalStateException("Delta is not configured in the Spark session.");
        });

        Assertions.assertEquals(Optional.empty(), type);
    }

    private static FunctionCallExpression deltaFileCall() {
        return new FunctionCallExpression(
                new Name(Name.JN_NS, "jn", "delta-file"),
                List.of(new StringLiteralExpression(DELTA_TABLE.getAbsolutePath(), ExceptionMetadata.EMPTY_METADATA)),
                ExceptionMetadata.EMPTY_METADATA);
    }

    private static ReturnTypeContext context() {
        RumbleConfiguration configuration = RumbleConfiguration.defaultConfiguration();
        StaticContext staticContext =
                new StaticContext(DELTA_TABLE.getParentFile().toURI(), configuration);
        return new ReturnTypeContext(null, configuration, null, staticContext, List.of());
    }
}
