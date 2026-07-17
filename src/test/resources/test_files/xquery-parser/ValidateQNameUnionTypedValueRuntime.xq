(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(
  validate type ex:QNameOrInteger {
    <value xmlns:q="urn:example:qname">q:item</value>
  }
)
return $value instance of xs:QName and namespace-uri-from-QName($value) eq "urn:example:qname"
