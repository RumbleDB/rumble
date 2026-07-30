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


package org.rumbledb.runtime.functions.json;

import com.google.gson.stream.JsonReader;
import org.rumbledb.api.Item;
import org.rumbledb.cli.ConsoleOutput;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.parsing.ItemParser;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.items.parsing.JSONParsingOptions;

import java.io.Serial;
import java.io.StringReader;
import java.util.List;

public class ParseJsonFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public ParseJsonFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return ComputedLocalCursor.fromArguments(
            this.getChildren(),
            context,
            arguments -> evaluate(arguments, context),
            getMetadata()
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        return evaluate(
            ComputedLocalCursor.arguments(
                this.getChildren().size(),
                index -> this.getChild(index).materializeFirstItemOrNull(context)
            ),
            context
        );
    }

    private Item evaluate(ComputedLocalCursor.Arguments<Item> arguments, DynamicContext context) {
        Item stringItem = arguments.get(0);
        Item optionsItem = arguments.size() > 1 ? arguments.get(1) : null;
        if (stringItem == null) {
            return null;
        }
        boolean isJSONiq10 = this.staticContext.getQueryLanguage().equals("jsoniq10");
        JSONParsingOptions options = JSONParsingOptions.resolveOptions(
            optionsItem,
            isJSONiq10,
            context,
            this.staticContext,
            getMetadata()
        );
        if (options.isLegacy()) {
            ConsoleOutput.warn(
                "Warning: fn:parse-json option 'legacy' skips spec-conformant JSON parsing in favor of "
                    + "RumbleDB's previous Gson-based parser and may produce unexpected results; "
                    + "retry without it if the output looks wrong."
            );
            return ItemParser.getItemFromObject(
                new JsonReader(new StringReader(stringItem.getStringValue())),
                isJSONiq10,
                options.getNumberFormat(),
                getMetadata(),
                false
            );
        }
        return ItemParser.getItemFromJSONString(
            stringItem.getStringValue(),
            options,
            this.staticContext.getConfiguration().getXmlVersion(),
            isJSONiq10,
            getMetadata()
        );
    }
}
