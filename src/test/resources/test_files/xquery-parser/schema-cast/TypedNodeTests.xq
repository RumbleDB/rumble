(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:typed-node-test" at "TypedNodeTests.xsd";

let $root := validate strict {
    <t:root count="4"><t:amount>42</t:amount></t:root>
}
let $document := validate strict {
    document { <t:root count="4"><t:amount>42</t:amount></t:root> }
}
let $attribute := $root/@count
let $nilled := validate strict {
    <t:root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" count="0" xsi:nil="true"/>
}
return (
    $root instance of element(t:root, t:Record),
    $root instance of element(*, t:Record),
    $root instance of element(t:root, xs:anyType),
    not($root instance of element(t:other, t:Record)),
    $attribute instance of attribute(count, t:Count),
    $attribute instance of attribute(*, xs:integer),
    $document instance of document-node(element(t:root, t:Record)),
    not($nilled instance of element(t:root, t:Record)),
    $nilled instance of element(t:root, t:Record?),
    (<plain/> instance of element(*, xs:untyped))
    and not($nilled instance of element(*, xs:numeric?))
    and ($root/t:amount instance of element(*, xs:numeric))
    and not(document { <plain/>, text { "extra" } } instance of document-node(element(*)))
)
