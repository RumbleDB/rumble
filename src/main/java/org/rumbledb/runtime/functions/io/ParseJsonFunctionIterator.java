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
 * Authors: Ghislain Fourny
 *
 */


package org.rumbledb.runtime.functions.io;

import com.google.gson.stream.JsonReader;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.json.JSONParsingOptions;

import java.io.StringReader;
import java.util.List;

public class ParseJsonFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public ParseJsonFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        Item stringItem = this.children.get(0).materializeFirstItemOrNull(context);
        Item optionsItem = this.children.size() > 1 ? this.children.get(1).materializeFirstItemOrNull(context) : null;
        if (stringItem == null) {
            return null;
        }
        boolean isJSONiq = getConfiguration().getQueryLanguage().equals("jsoniq10");
        JSONParsingOptions options = JSONParsingOptions.resolveOptions(optionsItem, isJSONiq, getMetadata());
        if (optionsItem == null) {
            try {
                JsonReader object = new JsonReader(new StringReader(stringItem.getStringValue()));
                return ItemParser.getItemFromObject(object, isJSONiq, options.getNumberFormat(), getMetadata());
            } catch (Exception e) {
                return ItemParser.getItemFromJSONString(
                    stringItem.getStringValue(),
                    options,
                    staticContext.getConfiguration().getXmlVersion(),
                    isJSONiq,
                    getMetadata()
                );
            }
        }
        return ItemParser.getItemFromJSONString(
            stringItem.getStringValue(),
            options,
            staticContext.getConfiguration().getXmlVersion(),
            isJSONiq,
            getMetadata()
        );
    }
}
