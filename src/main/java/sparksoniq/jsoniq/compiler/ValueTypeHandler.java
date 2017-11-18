/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
 package sparksoniq.jsoniq.compiler;

import sparksoniq.jsoniq.compiler.translator.expr.primary.*;
import sparksoniq.jsoniq.compiler.parser.JsoniqParser;
import sparksoniq.jsoniq.compiler.translator.metadata.ExpressionMetadata;

import java.math.BigDecimal;


public class ValueTypeHandler {

    public static String getStringValue(JsoniqParser.StringLiteralContext ctx)
    {
        return ctx.getText().substring(1, ctx.getText().length() - 1);
    }

    public static PrimaryExpression getValueType(String token, ExpressionMetadata metadataFromContext)
    {
        switch (token){
            case "null":
                return new NullLiteral(metadataFromContext);
            case "true":
                return new BooleanLiteral(true, metadataFromContext);
            case "false":
                return new BooleanLiteral(false, metadataFromContext);
            default:
                return ValueTypeHandler.getNumericLiteral(token, metadataFromContext);
        }
    }

    //TODO think of beter way to distinguish numeric literals
    private static PrimaryExpression getNumericLiteral(String token, ExpressionMetadata metadataFromContext) {
        if(!token.contains(".") && !token.contains("e") && !token.contains("E"))
            return new IntegerLiteral(Integer.parseInt(token), metadataFromContext);
        if(!token.contains("e") && !token.contains("E"))
            return new DecimalLiteral(new BigDecimal(token), metadataFromContext);
        return new DoubleLiteral(Double.parseDouble(token), metadataFromContext);

    }


}
