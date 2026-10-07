(:JIQS: ShouldRun; Output="(1, 2.5, 1, 1, true, 1, 0, 4, 6)" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

let $order := validate {
    <p:order>
        <p:item id="1" note="first"><p:price>2.5</p:price><p:tag>a</p:tag></p:item>
        <p:special id="2"><p:price>1</p:price></p:special>
        <p:paid>true</p:paid>
        <p:customer><p:name>Ada</p:name></p:customer>
        <p:meta/>
    </p:order>
}
return (
    (: Members of the item substitution group have other names. :)
    count($order/p:item is statically element(p:item, p:Item)*),
    sum($order/p:item/p:price) is statically xs:decimal,
    sum($order/p:item/@id) is statically xs:integer,
    count(for $item in $order/p:item return $item/@note is statically attribute(note, xs:string)?),
    (: The choice selects paid or due. :)
    data($order/p:paid is statically element(p:paid, xs:boolean)?),
    count($order/p:customer is statically element(p:customer, p:Customer)),
    (: Customer instances may select Extended with xsi:type. :)
    count($order/p:customer/p:extra is statically element(p:extra, xs:integer)?),
    (: The wildcard may match any element from another namespace, so the step stays untyped. :)
    count($order/p:meta/* is statically element()*) + 4,
    count($order/* is statically element()+) + 1
)
