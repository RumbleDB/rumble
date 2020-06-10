/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"), you may not use this file except in compliance with
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

package org.rumbledb.errorcodes;

public enum ErrorCode {

    DivisionByZero("FOAR0001"),

    InvalidLexicalValueErrorCode("FOCA0002"),
    CodepointNotValidErrorCode("FOCH0001"),
    InvalidNormalizationForm("FOCH0003"),
    CannotRetrieveResourceErrorCode("FODC0002"),

    IncorrectSyntaxFormatDateTimeErrorCode("FOFD1340"),
    ComponentSpecifierNotAvailableErrorCode("FOFD1350"),

    CastErrorCode("FORG0001"),
    ZeroOrOneErrorCode("FORG0003"),
    OneOrMoreErrorCode("FORG0004"),
    ExactlyOneErrorCode("FORG0005"),
    InvalidArgumentType("FORG0006"),
    InvalidRegexPatternErrorCode("FORX0002"),
    MatchesEmptyStringErrorCode("FORX0003"),
    InvalidReplacementStringErrorCode("FORX0004"),



    DuplicatePairNameErrorCode("JNDY0003"),


    StringOfJSONiqItemsErrorCode("JNTY0024"),


    NonAtomicElementErrorCode("JNTY0004"),
    InvalidSelectorErrorCode("JNTY0018"),

    CannotMaterializeErrorCode("RBDY0005"),


    UnrecognizedRumbleMLClassReferenceErrorCode("RBML0001"),
    UnrecognizedRumbleMLParamReferenceErrorCode("RBML0002"),
    InvalidRumbleMLParamErrorCode("RBML0003"),
    MLNotADataFrameErrorCode("RBML0004"),
    MLInvalidDataFrameSchemaErrorCode("RBML0005"),


    CliErrorCode("RBST0001"),
    UnimplementedErrorCode("RBST0002"),
    JobWithinAJobErrorCode("RBST0003"),
    OurBadErrorCode("RBST0004"),


    FunctionsNonSerializable("SENR0001"),


    AbsentPartOfDynamicContextCode("XPDY0002"),
    DynamicTypeTreatErrorCode("XPDY0050"),
    RuntimeExceptionErrorCode("XPDY0130"),


    ParsingErrorCode("XPST0003"),
    UndeclaredVariableErrorCode("XPST0008"),
    InvalidFunctionCallErrorCode("XPST0017"),
    CastableErrorCode("XPST0080"),
    InvalidExceptionErrorCode("XPST0081"),


    UnexpectedTypeErrorCode("XPTY0004"),

    CycleInVariableDeclarationsErrorCode("XQDY0054"),

    ModuleDeclarationErrorCode("XQST0016"),
    InvalidJsoniqVersionErrorCode("XQST0031"),
    DuplicateFunctionIdentifier("XQST0034"),
    DuplicateParamName("XQST0039"),
    VariableAlreadyExists("XQST0049"),
    UnknownCastTypeErrorCode("XQST0052"),
    InvalidGroupVariableErrorCode("XQST0094"),
    InvalidDecimalFormatPictureStringErrorCode("FODF1310"),

    InvalidTimezoneValue("FODT0003");

    private String code;

    ErrorCode(String c) {
        this.code = c;
    }

    public String toString() {
        return this.code;
    }


}
