(:JIQS: ShouldRun; Output="true" :)
let $value := data(
  validate type xs:QName {
    <value xmlns="urn:example:qname">item</value>
  }
)
return
  $value instance of xs:QName
  and string($value) eq "item"
  and empty(prefix-from-QName($value))
  and local-name-from-QName($value) eq "item"
  and namespace-uri-from-QName($value) eq "urn:example:qname"
