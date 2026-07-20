(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(validate type ex:CodeList { <value>ABC DEF</value> })
return count($value) eq 2 and (every $code in $value satisfies $code instance of xs:token)
