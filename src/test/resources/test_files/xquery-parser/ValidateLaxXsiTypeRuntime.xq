(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $value := data(
  validate lax {
    <unknown
      xmlns:ex="urn:example:compiled-schema"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:type="ex:Code">ABC</unknown>
  }
)
return $value instance of xs:token and string($value) eq "ABC"
