(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(validate type xs:QName { <value xmlns:q="urn:example:qname">q:item</value> })
return
  $value instance of xs:QName
  and namespace-uri-from-QName($value) eq "urn:example:qname"
  and local-name-from-QName($value) eq "item"
  and prefix-from-QName($value) eq "q"
