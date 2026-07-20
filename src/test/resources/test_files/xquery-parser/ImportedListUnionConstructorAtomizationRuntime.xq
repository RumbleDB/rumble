(:JIQS: ShouldRun; Output="ABC,DEF" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
string-join(ex:CodeListOrInteger(<value>ABC DEF</value>), ",")
