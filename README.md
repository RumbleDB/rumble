Jsoniq on Spark

CURRENT STATUS: 
- Grammar, ANTLR generated parser + lexer, ANTLR generated AST;
- Expression Tree 
- Static Context (in scope variables only)
- Runtime iterators
- Working basic FLOWRs (for, let, where, return)

Unsupported/Unimplemented features
- prolog
- modules
- separate variable declarations
- switch/if/try/catch/quantified expressions
- string concat/cast/treat/instance of expressions
- advanced object lookup/context expression/ {||} pairs

FILES
- pom.xml - Maven Build File;
- build_antlr_parser.xml - ANT build File, generates Parser, Lexer, Tokens;
- src/test/java/iq/frontend/FrontendTests.java - JUnit Tests for Parser
- src/test/java/iq/frontend/RuntimeTests.java - JUnit Tests for local runtime iterators
- src/test/java/iq/frontend/SparkRuntimeTests.java - JUnit Tests for spark runtime iterators

SETUP
- Requirements: maven, Spark (tested on 2.1.0), org.json parser
- 1) Packaging (with requirements) - mvn clean compile assembly:single
- 2) Tests:     - mvn -Dtest=FrontendTests test &&     - mvn -Dtest=RuntimeTests test && - mvn -Dtest=SparkRuntimeTests test
