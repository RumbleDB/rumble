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

package org.rumbledb.items;


import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.ItemType;

import java.io.Serial;

public class NullItem implements Item {


    @Serial
    private static final long serialVersionUID = 1L;

    public NullItem() {
        super();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Item otherItem) {
            long c = ComparisonIterator.compareItems(
                this,
                otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public Item copy(boolean mutable) {
        return this;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Object getVariantValue() {
        return null;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }



    public int hashCode() {
        return 0;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.nullItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public String getStringValue() {
        return "null";
    }

    @Override
    public String getSparkSQLValue() {
        return "NULL";
    }

    @Override
    public String getSparkSQLValue(ItemType itemType) {
        return "CAST(NULL AS " + itemType.getSparkSQLType() + ")";
    }
}
