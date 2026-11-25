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
package org.rumbledb.items.parsing;

import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.UserDefinedType;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.flwor.udfs.DataFrameContext;

public class ItemUserDefinedType extends UserDefinedType<Item> {

    private transient DataFrameContext dataFrameContext;

    private void initialize() {
        this.dataFrameContext = new DataFrameContext();
    }

    @Override
    public DataType sqlType() {
        return DataTypes.BinaryType;
    }

    @Override
    public synchronized byte[] serialize(Item item) {
        if (this.dataFrameContext == null) {
            initialize();
        }
        this.dataFrameContext.getOutput().clear();
        this.dataFrameContext.getKryo().writeClassAndObject(this.dataFrameContext.getOutput(), item);
        return this.dataFrameContext.getOutput().toBytes();
    }

    @Override
    public synchronized Item deserialize(Object object) {
        if (this.dataFrameContext == null) {
            initialize();
        }
        if (object instanceof byte[] b) {
            this.dataFrameContext.getInput().setBuffer(b);
            Object result = this.dataFrameContext.getKryo().readClassAndObject(this.dataFrameContext.getInput());
            if (result instanceof Item) {
                return (Item) result;
            }
            throw new OurBadException("Expected an item but got " + result.getClass());
        }
        throw new OurBadException("Expected a byte array but got " + object.getClass());
    }

    @Override
    public Class<Item> userClass() {
        return Item.class;
    }
}
