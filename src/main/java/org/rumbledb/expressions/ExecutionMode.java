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
package org.rumbledb.expressions;

public enum ExecutionMode {
    UNSET,
    LOCAL,
    RDD,
    DATAFRAME;

    public boolean isRDDOrDataFrame() {
        return this == ExecutionMode.RDD || this == ExecutionMode.DATAFRAME;
    }

    public boolean isDataFrame() {
        return this == ExecutionMode.DATAFRAME;
    }

    public boolean isRDD() {
        return this == ExecutionMode.RDD;
    }

    public boolean isLocal() {
        return this == ExecutionMode.LOCAL;
    }

    public boolean isUnset() {
        return this == ExecutionMode.UNSET;
    }

    public String toString() {
        switch (this) {
            case UNSET:
                return "unset";
            case LOCAL:
                return "local";
            case RDD:
                return "rdd";
            case DATAFRAME:
                return "dataframe";
        }
        return null;
    }
}
