# Error codes

- [FODC0002] - Error retrieving resource.

- [JNDY0003] - Duplicate pair name. It is a dynamic error if two pairs in an object
 constructor or in a simple object union have the same name.

- [JNTY0004] - Unexpected non-atomic element. Raised when objects
or arrays are supplied where an atomic element is expected.

- [JNTY0018] - Invalid selector error code.
It is a type error if there is not exactly one supplied parameter
for an object or array selector.

- [SENR0001] - Serialization error. Function items can not be serialized

- [SPRKIQ0001] - CLI error. Raised when invalid parameters are supplied at launch.

- [SPRKIQ0002] - CLI error. Unimplemented feature error.
Raised when a JSONiq feature that is not yet implemented in Rumble is used.

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

- [XQST0016] - Module declaration error. 
Current implementation does not support the Module Feature 
raises a static error if it encounters a module declaration 
or a module import.

- [XQST0031] - Invalid JSONiq version. It is a static error 
if the version number specified in a version declaration 
is not supported by the implementation. For now, only version 1.0 is supported.

- [XQST0034] - Function already exists. It is a static error
if multiple functions declared or imported by a module have
the same number of arguments and their expanded QNames are equal
(as defined by the eq operator).

- [XQST0039] - Duplicate parameter name. It is a static error
for a function declaration or an inline function expression
to have more than one parameter with the same name.

- [XQST0052] - Simple type error. The type must be
the name of a type defined in the in-scope schema types,
and the {variety} of the type must be simple.

- [XQST0094] - Invalid variable in group-by clause. 
The name of each grouping variable must be equal 
(by the eq operator on expanded QNames) to the name of a 
variable in the input tuple stream.
