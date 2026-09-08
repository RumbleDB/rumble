(:JIQS: ShouldRun; Output="true" :)
declare construction strip;
declare copy-namespaces no-preserve, inherit;
import schema namespace t = "urn:construction" at "Construction.xsd";
let $source := validate strict { <t:holder xmlns:p="urn:value" q="p:code"/> }
let $copy := <wrapper>{ $source/@q }</wrapper>
let $doc := document { <a xml:id="one"/> }
return data($copy/@q) instance of xs:untypedAtomic and count(id("one", $doc)) eq 1
