(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
every $code in ("ABC DEF" cast as ex:CodeList) satisfies $code instance of ex:Code
