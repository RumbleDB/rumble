(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(
  validate type ex:QNameList {
    <value xmlns:q="urn:example:qname">q:first q:second</value>
  }
)
return
  count($value) eq 2
  and (every $name in $value satisfies namespace-uri-from-QName($name) eq "urn:example:qname")
