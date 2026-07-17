(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(
  validate type ex:Notice {
    <value xmlns:n="urn:example:compiled-schema">n:notice</value>
  }
)
return $value instance of xs:NOTATION and string($value) eq "n:notice"
