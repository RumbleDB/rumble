# Error codes

- [FOAR0001] - Division by zero.

- [FOAR0002] - Numeric operation overflow/underflow

- [FOCA0002] - A value that is not lexically valid for a particular type has been encountered.

- [FOCH0001] - Raised by fn:codepoints-to-string if the input contains an integer that is not the codepoint of a valid XML character.

- [FOCH0003] - Raised by fn:normalize-unicode if the requested normalization form is not supported by the implementation.

- [FODC0002] - Error retrieving resource.

- [FODT0001] - Overflow/underflow in date/time operation.

- [FODT0002] - Overflow/underflow in duration operation.

- [FOFD1340] -This error is raised if the picture string or calendar supplied to fn:format-date, fn:format-time, or fn:format-dateTime has invalid syntax.

- [FOFD1350] - This error is raised if the picture string supplied to fn:format-date selects a component that is not present in a date, or if the picture string supplied to fn:format-time selects a component that is not present in a time.

- [FOTY0012] - The argument has no typed value (objects, arrays, functions cannot be atomized).

- [JNTY0004] - Unexpected non-atomic element. Raised when objects
or arrays are supplied where an atomic element is expected.

- [JNTY0024] - Error getting the string value for array and object items

- [JNTY0018] - Invalid selector error code.
It is a type error if there is not exactly one supplied parameter
for an object or array selector.

- [RBDY0005] - Materialization Error: the sequence is too big to be materialized. Use --materialization-cap to increase the maximum  materialization size, or add an output path to write to.

- [RBML0001] - Unrecognized RumbleDB ML Class Reference
An unrecognized classname is used in query while accessing the RumbleDB ML API.

- [RBML0002] - Unrecognized RumbleDB ML Param Reference
An unrecognized parameter is used in query while operating with a RumbleDB ML class.

- [RBML0003] - Invalid RumbleDB ML Param
Provided parameter does not match the expected type or value for the referenced RumbleDB ML class.

- [RBML0004] - Input is not a DataFrame
Provided input of items does not form a DataFrame as expected by RumbleDB ML.

- [RBML0005] - Invalid schema for DataFrame in annotate()
The provided schema can not be applied to the item data while converting the data to a DataFrame

- [RBST0001] - CLI error. Raised when invalid parameters are supplied at launch.

- [RBST0002] - Unimplemented feature error.
Raised when a JSONiq feature that is not yet implemented in RumbleDB is used.

- [RBST0003] - Invalid for clause expression error.
Raised when an expression produces a different,
big sequence of items for each binding within a big tuple,
which would lead to a data flow explosion and to a nesting of jobs on the Spark cluster.

- [RBST0004] - Implementation Error.

- [SENR0001] - Serialization error. Function items can not be serialized.

- [XPDY0002] - It is a dynamic error if evaluation of an expression relies on some part of the dynamic context that is absent.

- [XPDY0050] - Dynamic type treat error. It is a dynamic error
if the dynamic type of the operand of a treat expression does not match
the sequence type specified by the treat expression. This error might
also be raised by a path expression beginning with "/" or "//"
if the context node is not in a tree that is rooted at a document node.
This is because a leading "/" or "//" in a path expression is an abbreviation
for an initial step that includes the clause treat as document-node().

- [XPDY0130] - Generic runtime exception [check error message].

- [XPST0003] - Parsing error.
Invalid syntax or unsupported feature in query.

- [XPST0008] - Undefined element reference. It is a static error
if an expression refers to an element name, attribute name, schema type name,
namespace prefix, or variable name that is not defined in the static context,
except for an ElementName in an ElementTest or an AttributeName in an AttributeTest.

- [XPST0017] - Invalid function call error.
It is a static error if the expanded QName and number
of arguments in a static function call do not match
the name and arity of a function signature in the static context.

- [XPST0080] - Invalid cast error - It is a static error
if the target type of a cast or castable expression is NOTATION anySimpleType, or anyAtomicType.

- [XPST0081] - Unknown namespace prefix - It is a static error
if a QName used in a query contains a namespace prefix that cannot be expanded
into a namespace URI by using the statically known namespaces.

- [XPTY0004] - Unexpected Type Error. 
It is a type error if, during the static analysis phase, 
an expression is found to have a static type that is not
appropriate for the context in which the expression occurs, 
or during the dynamic evaluation phase, the dynamic type of 
a value does not match a required type. 
Example: using subtraction on strings.

- [XQDY0054] - It is a dynamic error if a cycle is encountered
in the definition of a module's dynamic context components,
for example because of a cycle in variable declarations.

- [XQDY0137] - Duplicate pair name. It is a dynamic error if two pairs in an object
  constructor or in a simple object union have the same name.

- [XQST0016] - Module declaration error. 
Current implementation does not support the Module Feature 
raises a static error if it encounters a module declaration 
or a module import.

- [XQST0031] - Invalid JSONiq version. It is a static error 
if the version number specified in a version declaration 
is not supported by the implementation. For now, only version 1.0 is supported.

- [XQST0033] - Namespace prefix bound twice. It is a static error if
a module contains multiple bindings for the same namespace prefix.

- [XQST0034] - Function already exists. It is a static error
if multiple functions declared or imported by a module have
the same number of arguments and their expanded QNames are equal
(as defined by the eq operator).

- [XQST0038] - It is a static error if a Prolog contains more than one default
collation declaration, or the value specified by a default collation
declaration is not present in statically known collations.

- [XQST0039] - Duplicate parameter name. It is a static error
for a function declaration or an inline function expression
to have more than one parameter with the same name.

- [XQST0047] - It is a static error if multiple module imports
in the same Prolog specify the same target namespace.

- [XQST0048] - It is a static error if a function or variable declared
in a library module is not in the target namespace of the library module.

- [XQST0049] - It is a static error if two or more variables
declared or imported by a module have the same name.

- [XQST0052] - Simple type error. The type must be
the name of a type defined in the in-scope schema types,
and the {variety} of the type must be simple.

- [XQST0059] - It is a static error if an implementation is unable
to process a schema or module import by finding a schema or module with
the specified target namespace.

- [XQST0069] - A static error is raised if a Prolog contains more than
one empty order declaration.

- [XQST0088] - It is a static error if the literal that specifies
the target namespace in a module import or a module declaration is of
zero length.

- [XQST0089] - It is a static error if a variable bound in a for or
window clause of a FLWOR expression, and its associated positional
variable, do not have distinct names (expanded QNames).

- [XQST0094] - Invalid variable in group-by clause. 
The name of each grouping variable must be equal 
(by the eq operator on expanded QNames) to the name of a 
variable in the input tuple stream.

- [XQST0118] - In a direct element constructor, the name
used in the end tag must exactly match the name used in the
corresponding start tag, including its prefix or absence of
a prefix. 