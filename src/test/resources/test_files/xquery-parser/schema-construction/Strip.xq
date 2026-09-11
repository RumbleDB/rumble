(:JIQS: ShouldRun; Output="true" :)
declare construction strip;
import schema namespace t = "urn:construction" at "Construction.xsd";
let $source := validate strict {
 <t:root number="12" key="one" refs="one"><t:value>42</t:value><t:nil xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/></t:root>
}
let $direct := <wrapper>{ $source }</wrapper>
let $computed := element wrapper { $source }
let $document := document { $source }
let $attributeCopy := <wrapper>{ $source/@number }</wrapper>
return every $check in (
    $direct instance of element(*, xs:untyped),
    $computed instance of element(*, xs:untyped),
    data($direct) instance of xs:untypedAtomic,
    every $copy in ($direct/t:root, $computed/t:root, $document/t:root) satisfies (
        data($copy/t:value) instance of xs:untypedAtomic and
        data($copy/@number) instance of xs:untypedAtomic and
        not(nilled($copy/t:nil)) and
        not($copy is $source)
    ),
    data($attributeCopy/@number) instance of xs:untypedAtomic,
    (: Construction must not change the validated source. :)
    data($source/t:value) instance of xs:integer,
    nilled($source/t:nil),
    count(id("one", $document)) eq 0,
    count(idref("one", $document)) eq 0
) satisfies $check
