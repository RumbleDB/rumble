(:JIQS: ShouldRun; Output="true" :)
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $element := validate strict {
  <ex:nil-root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/>
}
return
  not($element instance of element(ex:nil-root, ex:CodeContent))
  and $element instance of element(ex:nil-root, ex:CodeContent?)
  and $element instance of element(*, ex:CodeContent?)
