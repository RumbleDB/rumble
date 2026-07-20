(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $element := validate strict { <ex:nil-root>ABC</ex:nil-root> }
let $root := validate strict { <ex:root status="ready">ABC</ex:root> }
return
  exists($element/self::element(ex:nil-root, ex:CodeContent))
  and exists($element/self::element(*, xs:anyType))
  and exists($root/@status/self::attribute(status, xs:string))
  and empty($root/@status/self::attribute(status, xs:integer))
