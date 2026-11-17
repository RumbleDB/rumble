(:JIQS: ShouldRun; Output="true" :)
(: Without a declaration, construction defaults to preserve: new elements get xs:anyType and copied nodes retain their types. :)
import schema namespace t = "urn:construction" at "Construction.xsd";
let $source := validate strict { <t:qname xmlns:p="urn:value">p:code</t:qname> }
return <wrapper>{ $source }</wrapper> instance of element(*, xs:anyType)
        and not(<e/> instance of element(*, xs:untyped))
        and data((document { $source })/t:qname) instance of xs:QName
