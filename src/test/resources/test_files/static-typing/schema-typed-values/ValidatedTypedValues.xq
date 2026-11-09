(:JIQS: ShouldRun; Output="(true, true, 5.5, 3, true, 2, true, true, true, true)" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

let $price := validate { <t:price>4.5</t:price> }
return (
    exists($price is statically schema-element(t:price)),
    exists(validate type t:Price { <total>4.5</total> } is statically element(total, t:Price)),
    ($price + 1) is statically xs:decimal,
    data(validate { <t:amount currency="CHF">3</t:amount> }) is statically t:Price,
    empty(data(validate { <t:optional-price xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:nil="true"/> })
        is statically t:Price?),
    count(data(validate { <t:tags>a b</t:tags> }) is statically xs:string*),
    data(validate { <t:note>a<t:b>b</t:b></t:note> }) is statically xs:untypedAtomic eq "ab",
    (: Marker instances may select Remark, so their typed value may be untyped text. :)
    empty(data(validate { <t:marker/> }) is statically xs:untypedAtomic?),
    (: Element-only content cannot be atomized, so the typed value stays unknown. :)
    try { data(validate { <t:order><t:price>1</t:price></t:order> }) is statically xs:anyAtomicType* }
    catch err:FOTY0012 { true() },
    exists(validate lax { <t:unknown/> } is statically element(t:unknown))
)
