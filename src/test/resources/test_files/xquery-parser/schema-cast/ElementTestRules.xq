(:JIQS: ShouldRun; Output="(true, true, true, true, true, true, true)" :)
import schema namespace t = "urn:typed-node-test" at "TypedNodeTests.xsd";
declare namespace alias = "urn:typed-node-test";

let $node := validate strict { <t:root count="4"><t:amount>42</t:amount></t:root> }
let $nilled := validate strict {
    <t:root xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" count="0" xsi:nil="true"/>
}
return (
    (: 1. element() and element(*) :)
    ($node instance of element()) and ($nilled instance of element(*))
        and not(42 instance of element()) and not($node/@count instance of element(*)),

    (: 2. element(N), including a nilled node and an undeclared element name. :)
    ($nilled instance of element(alias:root)) and (<plain/> instance of element(plain))
        and not($node instance of element(t:other)),

    (: 3. element(N, T) :)
    ($node instance of element(alias:root, t:Record))
        and ($node/t:amount instance of element(t:amount, xs:integer))
        and not($nilled instance of element(t:root, t:Record))
        and not($node instance of element(t:other, t:Record))
        and not($node instance of element(t:root, xs:string)),

    (: 4. element(N, T?) :)
    ($node instance of element(t:root, t:Record?))
        and ($nilled instance of element(t:root, t:Record?))
        and not($nilled instance of element(t:other, t:Record?))
        and not($nilled instance of element(t:root, xs:string?)),

    (: 5. element(*, T) :)
    ($node instance of element(*, t:Record))
        and ($node/t:amount instance of element(*, xs:numeric))
        and not($nilled instance of element(*, t:Record))
        and not($node instance of element(*, xs:string)),

    (: 6. element(*, T?) :)
    ($node instance of element(*, t:Record?))
        and ($nilled instance of element(*, t:Record?))
        and not($nilled instance of element(*, xs:string?)),

    (: Default annotation: xs:untyped derives from xs:anyType. :)
    (<plain/> instance of element(plain, xs:untyped))
        and (<plain/> instance of element(*, xs:anyType?))
        and not(<plain/> instance of element(*, xs:string))
)
