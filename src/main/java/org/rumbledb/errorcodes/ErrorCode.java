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
    InvalidNaNOperation("FOCA0005"),
    CodepointNotValidErrorCode("FOCH0001"),
    UnsupportedCollationExceptionCode("FOCH0002"),
    InvalidNormalizationForm("FOCH0003"),
    CannotRetrieveResourceErrorCode("FODC0002"),

    UnidentifiedErrorExceptionCode("FOER0000"),

    IncorrectSyntaxFormatDateTimeErrorCode("FOFD1340"),
    ComponentSpecifierNotAvailableErrorCode("FOFD1350"),

    CastErrorCode("FORG0001"),
    ZeroOrOneErrorCode("FORG0003"),
    OneOrMoreErrorCode("FORG0004"),
    ExactlyOneErrorCode("FORG0005"),
    InvalidArgumentType("FORG0006"),
    InconsistentTimezones("FORG0008"),
    InvalidRegexPatternErrorCode("FORX0002"),
    MatchesEmptyStringErrorCode("FORX0003"),
    InvalidReplacementStringErrorCode("FORX0004"),

    NoTypedValueErrorCode("FOTY0012"),



    DuplicatePairNameErrorCode("JNDY0003"),


    StringOfJSONiqItemsErrorCode("JNTY0024"),


    NonAtomicElementErrorCode("JNTY0004"),
    InvalidSelectorErrorCode("JNTY0018"),

    CannotMaterializeErrorCode("RBDY0005"),


    UnrecognizedRumbleMLClassReferenceErrorCode("RBML0001"),
    UnrecognizedRumbleMLParamReferenceErrorCode("RBML0002"),
    InvalidRumbleMLParamErrorCode("RBML0003"),
    MLNotADataFrameErrorCode("RBML0004"),


    CliErrorCode("RBST0001"),
    UnimplementedErrorCode("RBST0002"),
    JobWithinAJobErrorCode("RBST0003"),
    OurBadErrorCode("RBST0004"),
    ClusterConnectionErrorCode("RBDY0005"),

    UnexpectedStaticType("RBTY0001"),


    FunctionsNonSerializable("SENR0001"),


    AbsentPartOfDynamicContextCode("XPDY0002"),
    DynamicTypeTreatErrorCode("XPDY0050"),
    RuntimeExceptionErrorCode("XPDY0130"),


    ParsingErrorCode("XPST0003"),
    StaticallyInferredEmptySequenceNotFromCommaExpression("XPST0005"),
    UndeclaredVariableErrorCode("XPST0008"),
    InvalidFunctionCallErrorCode("XPST0017"),
    UndefinedTypeErrorCode("XPST0051"),
    CastableErrorCode("XPST0080"),
    PrefixCannotBeExpandedErrorCode("XPST0081"),


    UnexpectedTypeErrorCode("XPTY0004"),

    InvalidInstance("XQDY0027"),
    CycleInVariableDeclarationsErrorCode("XQDY0054"),

    InvalidSchemaErrorCode("XQST0012"),
    ModuleDeclarationErrorCode("XQST0016"),
    InvalidJsoniqVersionErrorCode("XQST0031"),
    NamespacePrefixBoundTwiceCode("XQST0033"),
    DuplicateFunctionIdentifier("XQST0034"),
    DefaultCollationExceptionCode("XQST0038"),
    DuplicateParamName("XQST0039"),
    DuplicateModuleTargetNamespace("XQST0047"),
    NamespaceDoesNotMatchModule("XQST0048"),
    VariableAlreadyExists("XQST0049"),
    UnknownCastTypeErrorCode("XQST0052"),
    ModuleNotFoundErrorCode("XQST0059"),
    MoreThanOneEmptyOrderDeclarationErrorCode("XQST0069"),
    EmptyModuleURIErrorCode("XQST0088"),
    PositionalVariableNameSameAsForVariable("XQST0089"),
    InvalidGroupVariableErrorCode("XQST0094"),

    AtomizationError("FOTY0012"),
    UnexpectedFunctionItem("FOTY0015"),
    ArithmeticOverflowOrUnderflow("FODT0002"),
    InvalidTimezoneValue("FODT0003");



    private String code;

    ErrorCode(String c) {
        this.code = c;
    }

    public String toString() {
        return this.code;
    }


}
