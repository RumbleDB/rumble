(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $element := validate strict {
  <ex:nillable-code-member
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:nil="true"/>
}
return
  $element instance of schema-element(ex:code-head)
  and $element instance of schema-element(ex:nillable-code-member)
