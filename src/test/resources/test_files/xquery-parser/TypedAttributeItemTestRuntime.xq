(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $attribute := (validate strict { <ex:root status="ready">ABC</ex:root> })/@status
return
  $attribute instance of attribute(status, xs:string)
  and $attribute instance of attribute(*, xs:anyAtomicType)
  and not($attribute instance of attribute(other, xs:string))
