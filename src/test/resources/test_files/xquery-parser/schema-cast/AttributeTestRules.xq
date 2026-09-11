(:JIQS: ShouldRun; Output="(true, true, true, true, true)" :)
import schema namespace t = "urn:typed-node-test" at "TypedNodeTests.xsd";
declare default element namespace "urn:typed-node-test";

let $node := validate strict { <root count="4"><amount>42</amount></root> }
let $attribute := $node/@count
return (
    (: 1. attribute() and attribute(*) :)
    ($attribute instance of attribute()) and ($attribute instance of attribute(*))
        and not($node instance of attribute()) and not(42 instance of attribute(*)),

    (: 2. attribute(N): unprefixed attribute names stay in no namespace. :)
    ($attribute instance of attribute(count))
        and ((attribute undeclared { "value" }) instance of attribute(undeclared))
        and not($attribute instance of attribute(t:count)),

    (: 3. attribute(N, T): unprefixed type names use the default element/type namespace. :)
    ($attribute instance of attribute(count, Count))
        and ($attribute instance of attribute(count, xs:integer))
        and not($attribute instance of attribute(other, Count))
        and not($attribute instance of attribute(count, xs:string)),

    (: 4. attribute(*, T) :)
    ($attribute instance of attribute(*, Count))
        and ($attribute instance of attribute(*, xs:numeric))
        and not($attribute instance of attribute(*, xs:string)),

    (: Default annotation: xs:untypedAtomic and its base types. :)
    ((attribute plain { "value" }) instance of attribute(plain, xs:untypedAtomic))
        and ((attribute plain { "value" }) instance of attribute(*, xs:anyAtomicType))
        and ((attribute plain { "value" }) instance of attribute(*, xs:anySimpleType))
        and ((attribute plain { "value" }) instance of attribute(*, xs:anyType))
        and not((attribute plain { "value" }) instance of attribute(*, xs:string))
)
