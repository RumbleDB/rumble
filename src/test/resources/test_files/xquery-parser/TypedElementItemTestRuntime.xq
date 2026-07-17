(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $element := validate strict { <ex:nil-root>ABC</ex:nil-root> }
return
  $element instance of element(ex:nil-root, ex:CodeContent)
  and $element instance of element(*, ex:CodeContent)
  and $element instance of element(*, ex:Code)
  and $element instance of element(*, xs:anyType)
