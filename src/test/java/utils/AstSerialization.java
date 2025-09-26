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

package utils;

import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.parser.jsoniq.JsoniqParser;

import java.util.Arrays;

public class AstSerialization {

    // all serialization strings
    public static final String[] SERIALIZATION_RULES_FULL = new String[] {
        "module",
        "mainModule",
        "libraryModule",
        "prolog",
        "defaultCollationDecl",
        "orderingModeDecl",
        "emptyOrderDecl",
        "decimalFormatDecl",
        "dfPropertyName",
        "moduleImport",
        "varDecl",
        "functionDecl",
        "paramList",
        "expr",
        "exprSingle",
        "flowrExpr",
        "forClause",
        "forVar",
        "letClause",
        "letVar",
        "whereClause",
        "groupByClause",
        "groupByVar",
        "orderByClause",
        "countClause",
        "quantifiedExpr",
        "quantifiedExprVar",
        "switchExpr",
        "switchCaseClause",
        "typeSwitchExpr",
        "caseClause",
        "ifExpr",
        "tryCatchExpr",
        "orExpr",
        "andExpr",
        "notExpr",

        "comparisonExpr",
        "stringConcatExpr",
        "rangeExpr",
        "additiveExpr",
        "multiplicativeExpr",
        "instanceOfExpr",
        "treatExpr",
        "castableExpr",
        "castExpr",
        "unaryExpr",
        "simpleMapExpr",
        "postFixExpr",
        "predicate",
        "objectLookup",
        "arrayLookup",
        "arrayUnboxing",
        "primaryExpr",
        "varRef",
        "parenthesizedExpr",
        "contextItemExpr",
        "orderedExpr",
        "unorderedExpr",
        "functionCall",
        "argumentList",
        "argument",
        "sequenceType",
        "objectConstructor",
        "itemType",
        "jSONItemTest",
        "atomicType",
        "pairConstructor",
        "arrayConstructor",
        "uriLiteral",
        "stringLiteral" };

    // contains serialization strings only for supported features
    public static final String[] SERIALIZATION_RULES_PARTIAL = new String[] {
        "module",
        "mainModule",
        "#",
        "#",
        "#",
        "#",
        "#",
        "#",
        "#",
        "#",
        "varDecl",
        "functionDecl",
        "paramList",
        "expr",
        "exprSingle",
        "flowrExpr",
        "forClause",
        "forVar",
        "letClause",
        "letVar",
        "whereClause",
        "groupByClause",
        "groupByVar",
        "orderByClause",
        "orderByExpr",
        "countClause",
        "quantifiedExpr",
        "quantifiedExprVar",
        "#",
        "#",
        "#",
        "#",
        "ifExpr",
        "#",
        "orExpr",
        "andExpr",
        "notExpr",

        "comparisonExpr",
        "stringConcatExpr",
        "rangeExpr",
        "additiveExpr",
        "multiplicativeExpr",
        "instanceOfExpr",
        "#",
        "#",
        "#",
        "unaryExpr",
        "#",
        "postFixExpr",
        "predicate",
        "objectLookup",
        "arrayLookup",
        "arrayUnboxing",
        "primaryExpr",
        "varRef",
        "parenthesizedExpr",
        "#",
        "#",
        "#",
        "functionCall",
        "argumentList",
        "argument",
        "sequenceType",
        "objectConstructor",
        "itemType",
        "jSONItemTest",
        "atomicType",
        "pairConstructor",
        "arrayConstructor",
        "uriLiteral",
        "stringLiteral" };

    public static boolean checkSerialization(
            MainModule mainModule,
            JsoniqParser.MainModuleContext context
    ) {
        // TODO: update to scripting.
        String antlrSerializedTree = context.program()
            .statementsAndOptionalExpr()
            .expr()
            .toStringTree(Arrays.asList(SERIALIZATION_RULES_PARTIAL));
        antlrSerializedTree = filterNotSupportedFeatures(antlrSerializedTree);
        String expressionTree = mainModule.toString();
        boolean isEqual = antlrSerializedTree.equals(expressionTree);
        if (!isEqual)
            System.out.println("Expected :" + antlrSerializedTree + ";ACTUAL: " + expressionTree);
        return isEqual;
    }

    // filter unsupported clauses
    private static String filterNotSupportedFeatures(String antlrSerializedTree) {
        while (true) {
            int startIndex = antlrSerializedTree.indexOf("(#");
            if (startIndex < 0)
                break;
            int stopIndex = findClosingBrace(antlrSerializedTree.toCharArray(), startIndex);
            antlrSerializedTree = antlrSerializedTree.substring(0, stopIndex)
                + antlrSerializedTree.substring(stopIndex + 1, antlrSerializedTree.length());
            antlrSerializedTree = antlrSerializedTree.replaceFirst(" \\(#", "");
        }
        return antlrSerializedTree;
    }

    private static int findClosingBrace(char[] text, int openPos) {
        int closePos = openPos;
        int counter = 1;
        while (counter > 0) {
            char c = text[++closePos];
            if (c == '(') {
                counter++;
            } else if (c == ')') {
                counter--;
            }
        }
        return closePos;
    }
}
