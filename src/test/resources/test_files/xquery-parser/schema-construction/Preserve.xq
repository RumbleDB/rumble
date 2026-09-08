(:JIQS: ShouldRun; Output="true" :)
declare construction preserve;
import schema namespace t = "urn:construction" at "Construction.xsd";
let $source := validate strict {
 <t:root number="12" key="one" refs="one"><t:value>42</t:value><t:nil xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/></t:root>
}
let $direct := <wrapper>{ $source }</wrapper>
let $computed := element wrapper { $source }
let $document := document { $source }
let $attributeCopy := <wrapper>{ $source/@number }</wrapper>
return every $check in (
    $direct instance of element(*, xs:anyType),
    $computed instance of element(*, xs:anyType),
    data($direct) instance of xs:untypedAtomic,
    every $copy in ($direct/t:root, $computed/t:root, $document/t:root) satisfies (
        data($copy/t:value) instance of xs:integer and
        data($copy/@number) instance of xs:integer and
        nilled($copy/t:nil) and
        not($copy is $source)
    ),
    data($attributeCopy/@number) instance of xs:integer,
    (: Construction must not change the validated source. :)
    data($source/t:value) instance of xs:integer,
    nilled($source/t:nil),
    count(id("one", $document)) eq 1,
    count(idref("one", $document)) eq 1
) satisfies $check
