/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.operational.base;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;

public abstract class ComparisonUtil {

    public static void checkBinaryOperation(Item left, Item right, String sign, ExceptionMetadata metadata) {
        if (!left.isAtomic()) {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    +
                    "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                left.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, metadata);
        }
        if (!right.isAtomic()) {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    +
                    "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                right.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, metadata);
        }
        if (
            (!left.isNumeric() && !left.isYearMonthDuration() && !left.isDayTimeDuration() && !left.hasDateTime())
                ||
                (!right.isNumeric()
                    && !right.isYearMonthDuration()
                    && !right.isDayTimeDuration()
                    && !right.hasDateTime())
        ) {
            throw new UnexpectedTypeException(
                    " \""
                        + sign
                        + "\": operation not possible with parameters of type \""
                        + left.getDynamicType().toString()
                        + "\" and \""
                        + right.getDynamicType().toString()
                        + "\"",
                    metadata
            );
        }
    }
}
