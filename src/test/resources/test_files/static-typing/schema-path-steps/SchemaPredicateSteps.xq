(:JIQS: ShouldRun; Output="(1, 1, 2.5)" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

let $order := validate {
    <p:order>
        <p:item id="1"><p:price>2.5</p:price></p:item>
        <p:paid>true</p:paid>
        <p:customer><p:name>Ada</p:name></p:customer>
        <p:meta/>
    </p:order>
}
return (
    (: The context item of a predicate has the type of the step that it filters. :)
    count($order/p:item[. is statically element(p:item, p:Item)]),
    count($order/p:item[p:price is statically element(p:price, xs:decimal) > 1]),
    sum($order/p:item[p:price > 1]/p:price)
)
