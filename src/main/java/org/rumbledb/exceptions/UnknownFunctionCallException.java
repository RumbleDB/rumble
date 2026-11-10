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
package org.rumbledb.exceptions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.errorcodes.ErrorCode;

public class UnknownFunctionCallException extends RumbleException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownFunctionCallException(Name fnName, int arity, ExceptionMetadata metadata) {
        this(fnName, arity, metadata, null);
    }

    public UnknownFunctionCallException(Name fnName, int arity, ExceptionMetadata metadata, StaticContext context) {
        super(buildMessage(fnName, arity, context), ErrorCode.InvalidFunctionCallErrorCode, metadata);
    }

    public UnknownFunctionCallException(FunctionIdentifier identifier, ExceptionMetadata metadata) {
        this(identifier, metadata, null);
    }

    public UnknownFunctionCallException(
            FunctionIdentifier identifier, ExceptionMetadata metadata, StaticContext context) {
        this(identifier.getName(), identifier.getArity(), metadata, context);
    }

    private static String buildMessage(Name fnName, int arity, StaticContext context) {
        String message =
                "Undefined function call; Entered function name and arity doesn't match a defined function signature: \""
                        + fnName
                        + "\" called with "
                        + parameters(arity)
                        + ".";
        List<Integer> arities = knownArities(fnName, context);
        if (arities.isEmpty()) {
            return message;
        }
        String expected = arities.stream().map(String::valueOf).collect(Collectors.joining(" or "));
        return message
                + " Expected "
                + expected
                + (arities.size() == 1 ? " " + noun(arities.get(0)) : " parameters")
                + ".";
    }

    private static String noun(int count) {
        return count == 1 ? "parameter" : "parameters";
    }

    private static String parameters(int count) {
        return count + " " + noun(count);
    }

    private static List<Integer> knownArities(Name fnName, StaticContext context) {
        boolean defaultNamespace = Name.JSONIQ_DEFAULT_FUNCTION_NS.equals(fnName.getNamespace());
        Set<Integer> arities = new TreeSet<>();
        for (FunctionIdentifier id :
                BuiltinFunctionCatalogue.getBuiltinFunctions().keySet()) {
            Name candidate = id.getName();
            if (candidate.getLocalName().equals(fnName.getLocalName())
                    && (defaultNamespace || Objects.equals(candidate.getNamespace(), fnName.getNamespace()))) {
                arities.add(id.getArity());
            }
        }
        if (context != null) {
            arities.addAll(context.getUserDefinedFunctionArities(fnName));
        }
        return new ArrayList<>(arities);
    }
}
