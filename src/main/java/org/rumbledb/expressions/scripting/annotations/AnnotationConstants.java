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
package org.rumbledb.expressions.scripting.annotations;

import org.rumbledb.context.Name;

public class AnnotationConstants {
    public static final Name PUBLIC = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "public");
    public static final Name PRIVATE = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "private");
    public static final Name SEQUENTIAL = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "sequential");
    public static final Name NON_SEQUENTIAL = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "nonsequential");
    public static final Name UPDATING = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "updating");
    public static final Name SIMPLE = new Name(Name.XQUERY_ANNOTATIONS_NS, "", "simple");

    public static final Name ASSIGNABLE = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "assignable");

    public static final Name NON_ASSIGNABLE = new Name(Name.JSONIQ_ANNOTATIONS_NS, "an", "nonassignable");
}
