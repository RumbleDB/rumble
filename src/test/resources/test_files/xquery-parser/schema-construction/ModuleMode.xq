(:JIQS: ShouldRun; Output="true" :)
declare construction strip;
import module namespace m = "urn:preserve-module" at "PreserveModule.xqm";
import schema namespace t = "urn:construction" at "Construction.xsd";
let $source := validate strict { <t:qname xmlns:p="urn:value">p:code</t:qname> }
let $result := m:wrap($source)
return $result instance of element(*, xs:anyType) and data($result/t:qname) instance of xs:QName
