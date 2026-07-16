(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(validate type ex:MixedContent { <value>before<ex:code>ABC</ex:code>after</value> })
return $value instance of xs:untypedAtomic and string($value) eq "beforeABCafter"
