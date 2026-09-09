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
package org.rumbledb.runtime.plan;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.runtime.dataframe.RuntimeDataFrame;

/**
 * Native DataFrame execution capability. Plans that do not implement this interface are converted centrally from
 * another supported representation.
 *
 * @param <T> the logical value represented by each DataFrame row
 */
public interface DataFrameRuntimePlan<T> {

    /**
     * Builds the DataFrame for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting DataFrame
     */
    RuntimeDataFrame<T> createNativeDataFrame(DynamicContext context);
}
