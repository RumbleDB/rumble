(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(
  validate type ex:QNameAlias {
    <value xmlns:q="urn:example:qname">q:item</value>
  }
)
return
  $value instance of xs:QName
  and string($value) eq "q:item"
  and prefix-from-QName($value) eq "q"
  and local-name-from-QName($value) eq "item"
  and namespace-uri-from-QName($value) eq "urn:example:qname"
