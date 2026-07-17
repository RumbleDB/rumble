(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(validate type ex:Code { <value>ABC</value> })
return $value instance of ex:Code and $value instance of xs:token
