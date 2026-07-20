(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
empty(
  data(
    validate strict {
      <ex:nil-root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
    }
  )
)
