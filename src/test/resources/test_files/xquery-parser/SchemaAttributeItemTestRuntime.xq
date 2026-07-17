(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $root := validate strict { <ex:root>ABC</ex:root> }
let $attribute := $root/@ex:qualified-status
return
  $attribute instance of schema-attribute(ex:qualified-status)
  and $root/attribute::schema-attribute(ex:qualified-status) = "qualified"
