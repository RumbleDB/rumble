<h1 align="center">Sparksoniq</h1>

<h3 align="center">JSON at the scale of Spark </h3>

<h5 align="center">A JSONiq engine to query large-scale JSON datasets stored on HDFS. Spark under the hood. Alpha release coming soon. </h5>


<a href="https://ibb.co/j5nnB5"><img src="https://preview.ibb.co/nQPpPQ/icon.png" alt="icon" border="0"></a>

<h5>REQUIREMENTS</h5>
- Spark 2.1.0
- ANTLRv4 4.6


<h5>SETUP</h5>
1. Generate ANTLR Lexer and Parser - run ant build on build_antlr_parser.xml

2. Packaging (with requirements) - mvn clean compile assembly:single

3. Submit jar using spark-submit [CLI TBD]

Example:

spark-submit --class sparksoniq.ShellStart   --master yarn-client  --deploy-mode client --num-executors 40 --conf spark.yarn.maxAppAttempts=1 --conf spark.ui.port=4051 --conf spark.executor.memory=10g --conf spark.executor.heartbeatInterval=3600s --conf spark.network.timeout=3600s  jsoniq-spark-app-1.0-jar-with-dependencies.jar --master yarn-client --result-size 1000



<h5>LICENSES</h5>

- Spark 2.1.0 Libraries - Apache License
- ANTLR v4 Framework - BSD License
- org.json parser - JSON License
- JLine 3.0.2 terminal framework - BSD License
- Kryo 4.0.0 serialization framework - BSD License

<h5>Unsupported/Unimplemented features</h5>
- prolog
- modules
- separate variable declarations
- try/catch expressions
- cast/treat expressions
- advanced object lookup
- positional variables


<h5>ERROR CODES</h5>
- [XPST0003] - Parsing error. 
Invalid syntax or unsupported feature in query.
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
- [XPST0017] - Invalid function call error. 
It is a static error if the expanded QName and number 
of arguments in a static function call do not match 
the name and arity of a function signature in the static context.
- [XQST0031] - Invalid JSONiq version. It is a static error 
if the version number specified in a version declaration 
is not supported by the implementation. For now, only version 1.0 is supported.
- [XQST0094] - Invalid variable in group-by clause. 
The name of each grouping variable must be equal 
(by the eq operator on expanded QNames) to the name of a 
variable in the input tuple stream.
- [JNDY0003] - Duplicate pair name. t is a dynamic error if two pairs in an object
 constructor or in a simple object union have the same name.
- [JNTY0004] - Unexpected non-atomic element. Raised when objects 
or arrays are supplied where an atomic element is expected. 
- [JNTY0018] - Invalid selector error code. 
It is a type error if there is not exactly one supplied parameter 
for an object or array selector.
- [XPDY0139] - Unimplemented feature error. Raised when a JSONiq feature 
that is not yet implemented in Sparksoniq is used.
- [XPDY0140] - Undeclared variable error.
- [XPDY0130] - Generic runtime exception [check error message].
- [SPRKIQ0001] - CLI error. Raised when invalid parameters are supplied at launch.