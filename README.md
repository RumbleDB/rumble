<h1 align="center">Sparksoniq</h1>

<h3 align="center">JSON at the scale of Spark </h3>

<h5 align="center">A JSONiq engine to query large-scale JSON datasets stored on HDFS. Spark under the hood. Alpha release coming soon. </h5>


<a href="https://ibb.co/j5nnB5"><img src="https://preview.ibb.co/nQPpPQ/icon.png" alt="icon" border="0"></a>

<h5>REQUIREMENTS</h5>
- Spark 1.6.2
- ANTLRv4


<h5>SETUP</h5>
1) Generate ANTLR Lexer and Parser - run ant build on build_antlr_parser.xml

2) Packaging (with requirements) - mvn clean compile assembly:single

3) Submit jar using spark-submit [CLI TBD]

Example:

spark-submit --class jiqs.ShellStart     --master yarn-client     --deploy-mode client --num-executors 40 --conf spark.yarn.maxAppAttempts=1 --conf spark.ui.port=4051  --conf spark.executor.memory=10g --conf spark.executor.heartbeatInterval=3600s --conf spark.network.timeout=3600s  jsoniq-spark-app-1.0-jar-with-dependencies.jar yarn-client 1000


<h5>LICENSES</h5>
- Spark 1.6.2 Libraries - Apache License

- ANTLR v4 Framework - BSD License

- org.json parser - JSON License

- JLine 3.0.2 terminal framework - BSD License

- Kryo 4.0.0 serialization framework - BSD License

<h5>Unsupported/Unimplemented features</h5>
- prolog
- modules
- separate variable declarations
- switch/try/catchexpressions
- string concat/cast/treat/instance of expressions
- advanced object lookup
- positional variables

