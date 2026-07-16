(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $attribute := (validate strict { <ex:root>ABC</ex:root> })/@ex:qualified-status
return string($attribute) eq "qualified" and exists(prefix-from-QName(node-name($attribute)))
