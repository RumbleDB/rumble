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
 * Authors: Stefan Irimescu, Can Berker Cikis, Matteo Agnoletto (EPMatt)
 *
 */

package org.rumbledb.errorcodes;

import org.rumbledb.context.Name;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ErrorCode implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_NS = "http://www.w3.org/2005/xqt-errors";
    public static final String ERROR_PREFIX = "err";
    private static final Map<String, ErrorCode> BUILTIN_BY_IDENTIFIER = new HashMap<>();

    private final Name name;

    public ErrorCode(Name name) {
        this.name = Objects.requireNonNull(name, "Error code name cannot be null");
    }

    private static ErrorCode registerBuiltIn(String identifier) {
        ErrorCode errorCode = new ErrorCode(new Name(ERROR_NS, ERROR_PREFIX, identifier));
        BUILTIN_BY_IDENTIFIER.put(identifier, errorCode);
        return errorCode;
    }

    public Name getName() {
        return this.name;
    }

    public String getLocalName() {
        return this.name.getLocalName();
    }

    @Override
    public String toString() {
        return this.name.getLocalName();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ErrorCode)) {
            return false;
        }
        return this.name.equals(((ErrorCode) other).name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public static final ErrorCode DivisionByZero = registerBuiltIn("FOAR0001");
    public static final ErrorCode NumericOverflowOrUnderflow = registerBuiltIn("FOAR0002");

    public static final ErrorCode ArrayIndexOutOfBoundsErrorCode = registerBuiltIn("FOAY0001");
    public static final ErrorCode ArrayInvalidSubarrayLengthErrorCode = registerBuiltIn("FOAY0002");

    public static final ErrorCode InvalidLexicalValueErrorCode = registerBuiltIn("FOCA0002");
    public static final ErrorCode InvalidNaNOperation = registerBuiltIn("FOCA0005");
    public static final ErrorCode CodepointNotValidErrorCode = registerBuiltIn("FOCH0001");
    public static final ErrorCode UnsupportedCollationExceptionCode = registerBuiltIn("FOCH0002");
    public static final ErrorCode InvalidNormalizationForm = registerBuiltIn("FOCH0003");
    public static final ErrorCode CannotRetrieveResourceErrorCode = registerBuiltIn("FODC0002");

    public static final ErrorCode UnidentifiedErrorExceptionCode = registerBuiltIn("FOER0000");

    public static final ErrorCode IncorrectSyntaxFormatDateTimeErrorCode = registerBuiltIn("FOFD1340");
    public static final ErrorCode ComponentSpecifierNotAvailableErrorCode = registerBuiltIn("FOFD1350");

    public static final ErrorCode IncorrectSyntaxFormatNumberErrorCode = registerBuiltIn("FODF1310");
    public static final ErrorCode InvalidDecimalFormatName = registerBuiltIn("FODF1280");

    public static final ErrorCode CastErrorCode = registerBuiltIn("FORG0001");
    public static final ErrorCode ZeroOrOneErrorCode = registerBuiltIn("FORG0003");
    public static final ErrorCode OneOrMoreErrorCode = registerBuiltIn("FORG0004");
    public static final ErrorCode ExactlyOneErrorCode = registerBuiltIn("FORG0005");
    public static final ErrorCode InvalidArgumentType = registerBuiltIn("FORG0006");
    public static final ErrorCode InconsistentTimezones = registerBuiltIn("FORG0008");
    public static final ErrorCode InvalidRegexPatternErrorCode = registerBuiltIn("FORX0002");
    public static final ErrorCode MatchesEmptyStringErrorCode = registerBuiltIn("FORX0003");
    public static final ErrorCode InvalidReplacementStringErrorCode = registerBuiltIn("FORX0004");

    public static final ErrorCode FunctionAtomizationErrorCode = registerBuiltIn("FOTY0013");
    public static final ErrorCode FunctionItemStringValueErrorCode = registerBuiltIn("FOTY0014");

    public static final ErrorCode DuplicatePairNameErrorCode = registerBuiltIn("XQDY0137");

    public static final ErrorCode InvalidJSONErrorCode = registerBuiltIn("FOJS0001");
    public static final ErrorCode DuplicateJSONKeyErrorCode = registerBuiltIn("FOJS0003");
    public static final ErrorCode InvalidOptionErrorCode = registerBuiltIn("FOJS0005");
    public static final ErrorCode UnavailableResourceErrorCode = registerBuiltIn("FOUT1170");

    public static final ErrorCode StringOfJSONiqItemsErrorCode = registerBuiltIn("JNTY0024");

    public static final ErrorCode NonAtomicElementErrorCode = registerBuiltIn("JNTY0004");
    public static final ErrorCode InvalidSelectorErrorCode = registerBuiltIn("JNTY0018");

    public static final ErrorCode CannotMaterializeErrorCode = registerBuiltIn("RBDY0005");

    public static final ErrorCode UnrecognizedRumbleMLClassReferenceErrorCode = registerBuiltIn("RBML0001");
    public static final ErrorCode UnrecognizedRumbleMLParamReferenceErrorCode = registerBuiltIn("RBML0002");
    public static final ErrorCode InvalidRumbleMLParamErrorCode = registerBuiltIn("RBML0003");
    public static final ErrorCode MLNotADataFrameErrorCode = registerBuiltIn("RBML0004");

    public static final ErrorCode CliErrorCode = registerBuiltIn("RBST0001");
    public static final ErrorCode UnimplementedErrorCode = registerBuiltIn("RBST0002");
    public static final ErrorCode JobWithinAJobErrorCode = registerBuiltIn("RBST0003");
    public static final ErrorCode OurBadErrorCode = registerBuiltIn("RBST0004");
    public static final ErrorCode ClusterConnectionErrorCode = registerBuiltIn("RBDY0005");
    public static final ErrorCode DatesWithTimezonesNotSupported = registerBuiltIn("RBDY0006");
    public static final ErrorCode CannotModifyImmutableValue = registerBuiltIn("RBDY0007");
    public static final ErrorCode CannotInferSchemaOnNonStructuredData = registerBuiltIn("RBDY0008");

    public static final ErrorCode UnexpectedStaticType = registerBuiltIn("RBTY0001");

    public static final ErrorCode FunctionsNonSerializable = registerBuiltIn("SENR0001");

    public static final ErrorCode AbsentPartOfDynamicContextCode = registerBuiltIn("XPDY0002");
    public static final ErrorCode DynamicTypeTreatErrorCode = registerBuiltIn("XPDY0050");
    public static final ErrorCode RuntimeExceptionErrorCode = registerBuiltIn("XPDY0130");

    public static final ErrorCode ParsingErrorCode = registerBuiltIn("XPST0003");
    public static final ErrorCode StaticallyInferredEmptySequenceNotFromCommaExpression = registerBuiltIn("XPST0005");
    public static final ErrorCode UndeclaredVariableErrorCode = registerBuiltIn("XPST0008");
    public static final ErrorCode InvalidFunctionCallErrorCode = registerBuiltIn("XPST0017");
    public static final ErrorCode UndefinedTypeErrorCode = registerBuiltIn("XPST0051");
    public static final ErrorCode CastableErrorCode = registerBuiltIn("XPST0080");
    public static final ErrorCode PrefixCannotBeExpandedErrorCode = registerBuiltIn("XPST0081");

    public static final ErrorCode UnexpectedTypeErrorCode = registerBuiltIn("XPTY0004");
    public static final ErrorCode NodeAndNonNode = registerBuiltIn("XTPY0018");
    public static final ErrorCode UnexpectedNode = registerBuiltIn("XPTY0019");

    public static final ErrorCode InvalidInstance = registerBuiltIn("XQDY0027");
    public static final ErrorCode InvalidProcessingInstructionTargetCastErrorCode = registerBuiltIn("XQDY0041");
    public static final ErrorCode CycleInVariableDeclarationsErrorCode = registerBuiltIn("XQDY0054");
    public static final ErrorCode InvalidProcessingInstructionContentErrorCode = registerBuiltIn("XQDY0026");
    public static final ErrorCode InvalidProcessingInstructionTargetErrorCode = registerBuiltIn("XQDY0064");

    public static final ErrorCode InvalidSchemaErrorCode = registerBuiltIn("XQST0012");
    /**
     * Namespace declaration attribute value contains an enclosed expression (direct
     * element constructor).
     */
    public static final ErrorCode NamespaceDeclarationAttributeEnclosedExpressionErrorCode = registerBuiltIn(
        "XQST0022"
    );
    public static final ErrorCode ModuleDeclarationErrorCode = registerBuiltIn("XQST0016");
    public static final ErrorCode InvalidJsoniqVersionErrorCode = registerBuiltIn("XQST0031");
    public static final ErrorCode MultipleBaseURIExceptionCode = registerBuiltIn("XQST0032");
    public static final ErrorCode NamespacePrefixBoundTwiceCode = registerBuiltIn("XQST0033");
    public static final ErrorCode DuplicateFunctionIdentifier = registerBuiltIn("XQST0034");
    public static final ErrorCode DefaultCollationExceptionCode = registerBuiltIn("XQST0038");
    public static final ErrorCode DuplicateParamName = registerBuiltIn("XQST0039");
    public static final ErrorCode DuplicateModuleTargetNamespace = registerBuiltIn("XQST0047");
    public static final ErrorCode NamespaceDoesNotMatchModule = registerBuiltIn("XQST0048");
    public static final ErrorCode VariableAlreadyExists = registerBuiltIn("XQST0049");
    public static final ErrorCode UnknownCastTypeErrorCode = registerBuiltIn("XQST0052");
    public static final ErrorCode ModuleNotFoundErrorCode = registerBuiltIn("XQST0059");
    public static final ErrorCode MoreThanOneEmptyOrderDeclarationErrorCode = registerBuiltIn("XQST0069");
    public static final ErrorCode PredefinedPrefixInNamespaceDeclarationErrorCode = registerBuiltIn("XQST0070");
    public static final ErrorCode EmptyModuleURIErrorCode = registerBuiltIn("XQST0088");
    public static final ErrorCode PositionalVariableNameSameAsForVariable = registerBuiltIn("XQST0089");
    public static final ErrorCode InvalidGroupVariableErrorCode = registerBuiltIn("XQST0094");
    public static final ErrorCode DirectElementConstructorTagMismatchErrorCode = registerBuiltIn("XQST0118");
    public static final ErrorCode InvalidDecimalFormatPropertyConflict = registerBuiltIn("XQST0098");

    public static final ErrorCode AtomizationError = registerBuiltIn("FOTY0012");
    public static final ErrorCode UnexpectedFunctionItem = registerBuiltIn("FOTY0015");
    public static final ErrorCode DatetimeOverflowOrUnderflow = registerBuiltIn("FODT0001");
    public static final ErrorCode DurationOverflowOrUnderflow = registerBuiltIn("FODT0002");
    public static final ErrorCode InvalidTimezoneValue = registerBuiltIn("FODT0003");

    public static final ErrorCode InvalidUpdatingExpressionPositionErrorCode = registerBuiltIn("XUST0001");
    public static final ErrorCode SimpleExpressionMustBeVacuousErrorCode = registerBuiltIn("XUST0002");

    public static final ErrorCode TransformBadCopySource = registerBuiltIn("XUTY0013");

    public static final ErrorCode TransformModifiesNonCopiedValue = registerBuiltIn("XUDY0014");
    public static final ErrorCode UpdateTargetIsEmptySeqErrorCode = registerBuiltIn("XUDY0027");

    public static final ErrorCode UpdatingFunctionHasReturnTypeErrorCode = registerBuiltIn("XUST0028");

    public static final ErrorCode InvalidElementNameExpressionErrorCode = registerBuiltIn("XQDY0074");
    public static final ErrorCode InvalidCommentContentErrorCode = registerBuiltIn("XQDY0072");
    public static final ErrorCode InvalidNodeNameErrorCode = registerBuiltIn("XQDY0096");
    public static final ErrorCode DuplicateAttributeErrorCode = registerBuiltIn("XQDY0025");
    public static final ErrorCode InvalidComputedNamespaceConstructorErrorCode = registerBuiltIn("XQDY0101");
    public static final ErrorCode AttributeOrNamespaceAfterNonAttributeErrorCode = registerBuiltIn("XQTY0024");

    public static final ErrorCode DuplicateObjectInsertSourceErrorCode = registerBuiltIn("JNUP0005");
    public static final ErrorCode DuplicateKeyOnUpdateApplyErrorCode = registerBuiltIn("JNUP0006");
    public static final ErrorCode CannotCastUpdateSelectorErrorCode = registerBuiltIn("JNUP0007");
    public static final ErrorCode InvalidUpdateTargetErrorCode = registerBuiltIn("JNUP0008");
    public static final ErrorCode TooManyReplacesOnSameTargetSelectorErrorCode = registerBuiltIn("JNUP0009");
    public static final ErrorCode TooManyRenamesOnSameTargetSelectorErrorCode = registerBuiltIn("JNUP0010");
    public static final ErrorCode TooManyCollectionCreationsOnSameTargetException = registerBuiltIn("JNUP0011");
    public static final ErrorCode TooManyEditsOnSameTargetException = registerBuiltIn("JNUP0012");
    public static final ErrorCode CannotResolveUpdateSelectorErrorCode = registerBuiltIn("JNUP0016");
    public static final ErrorCode ObjectInsertContentIsNotObjectSeqErrorCode = registerBuiltIn("JNUP0019");

    public static final ErrorCode InvalidExpressionClassification = registerBuiltIn("SCCL0001");
    public static final ErrorCode InvalidComposabilityUpdatingAndSequentialExpression = registerBuiltIn("SCCP0001");
    public static final ErrorCode InvalidUpdatingExpressionOperand = registerBuiltIn("SCCP0002");

    public static final ErrorCode InvalidUpdatingExpressionCondition = registerBuiltIn("SCCP0003");
    public static final ErrorCode InvalidControlStatementComposability = registerBuiltIn("SCCP0004");
    public static final ErrorCode InvalidAssignableVariableComposability = registerBuiltIn("SCCP0005");
    public static final ErrorCode InvalidSequentialChildInNonSequentialParent = registerBuiltIn("SCCP0006");
    public static final ErrorCode InvalidAnnotation = registerBuiltIn("XQAN0001");
    public static final ErrorCode InvalidVariableDeclaration = registerBuiltIn("SCIN0001");

    public static final ErrorCode InvalidSerializationParameterValue = registerBuiltIn("SEPM0016");
}
