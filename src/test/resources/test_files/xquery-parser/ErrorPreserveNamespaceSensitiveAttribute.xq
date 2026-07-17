(:JIQS: ShouldCrash; ErrorCode="XQTY0086" :)
declare construction preserve;
import schema namespace ex = "urn:example:compiled-schema" at "schema-import-example.xsd";
let $validated := validate strict {
  <ex:qname-attribute-root xmlns:q="urn:example:qname" value="q:item"/>
}
return <wrapper>{$validated/@value}</wrapper>
