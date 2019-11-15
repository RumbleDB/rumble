/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package sparksoniq.exceptions.codes;

public class ErrorCodes {

    public static final String ErrorRetrievingResourceCode = "FODC0002";

    public static final String CastErrorCode = "FORG0001";
    public static final String ZeroOrOneErrorCode = "FORG0003";
    public static final String OneOrMoreErrorCode = "FORG0004";
    public static final String ExactlyOneErrorCode = "FORG0005";
    public static final String InvalidArgumentType = "FORG0006";


    public static final String DuplicatePairNameErrorCode = "JNDY0003";


    public static final String NonAtomicElementErrorCode = "JNTY0004";
    public static final String InvalidSelectorErrorCode = "JNTY0018";


    public static final String FunctionsNonSerializable = "SENR0001";


    public static final String CliErrorCode = "SPRKIQ0001";
    public static final String UnimplementedErrorCode = "SPRKIQ0002";


    public static final String AbsentPartOfDynamicContextCode = "XPDY0002";
    public static final String DynamicTypeTreatErrorCode = "XPDY0050";
    public static final String RuntimeExceptionErrorCode = "XPDY0130";


    public static final String ParsingErrorCode = "XPST0003";
    public static final String UndeclaredVariableErrorCode = "XPST0008";
    public static final String InvalidFunctionCallErrorCode = "XPST0017";
    public static final String CastableErrorCode = "XPST0080";
    public static final String InvalidExceptionErrorCode = "XPST0081";


    public static final String UnexpectedTypeErrorCode = "XPTY0004";


    public static final String ModuleDeclarationErrorCode = "XQST0016";
    public static final String InvalidJsoniqVersionErrorCode = "XQST0031";
    public static final String DuplicateFunctionIdentifier = "XQST0034";
    public static final String DuplicateParamName = "XQST0039";
    public static final String UnknownCastTypeErrorCode = "XQST0052";
    public static final String InvalidGroupVariableErrorCode = "XQST0094";

    public static final String InvalidTimezoneValue = "FODT0003";
}
