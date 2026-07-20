(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
every $code in ex:CodeList("ABC DEF") satisfies $code instance of ex:Code
