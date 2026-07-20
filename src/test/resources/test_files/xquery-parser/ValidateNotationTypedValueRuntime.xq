(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $left := data(
  validate type ex:Notice {
    <value xmlns:a="urn:example:compiled-schema">a:notice</value>
  }
)
let $right := data(
  validate type ex:Notice {
    <value xmlns:b="urn:example:compiled-schema">b:notice</value>
  }
)
return
  $left instance of xs:NOTATION
  and not($left instance of xs:QName)
  and string($left) eq "a:notice"
  and $left eq $right
  and map { $left : "found" }($right) eq "found"
