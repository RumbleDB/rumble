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

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.context.DynamicContext;

/**
 * Native RDD execution capability. Plans that do not implement this interface are converted centrally from another
 * supported representation.
 *
 * @param <T> the RDD element type
 */
public interface RDDRuntimePlan<T> {

    /**
     * Builds the RDD for one evaluation.
     *
     * @param context the dynamic context for that evaluation
     * @return the resulting RDD
     */
    JavaRDD<T> createNativeRDD(DynamicContext context);
}
