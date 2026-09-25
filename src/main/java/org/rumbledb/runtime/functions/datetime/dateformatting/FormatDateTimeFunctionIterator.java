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
package org.rumbledb.runtime.functions.datetime.dateformatting;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;

public class FormatDateTimeFunctionIterator extends DateFormattingFunctionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public FormatDateTimeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    protected OffsetDateTime extractTemporalValue(Item valueItem) {
        return valueItem.getDateTimeValue();
    }

    @Override
    protected String temporalTypeName() {
        return "dateTime";
    }

    @Override
    protected boolean supportsComponent(char component) {
        return component == 'Y'
                || component == 'M'
                || component == 'D'
                || component == 'd'
                || component == 'F'
                || component == 'W'
                || component == 'w'
                || component == 'H'
                || component == 'h'
                || component == 'P'
                || component == 'm'
                || component == 's'
                || component == 'f'
                || component == 'Z'
                || component == 'z'
                || component == 'C'
                || component == 'E';
    }
}
