(:JIQS: ShouldRun; Output="1" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

let $order := validate {
    <p:order>
        <p:item id="1"><p:price>2.5</p:price></p:item>
        <p:paid>true</p:paid>
        <p:customer><p:name>Ada</p:name></p:customer>
        <p:meta/>
    </p:order>
}
(: The context item of a predicate has the type of the step that it filters. :)
return count($order/p:item[. is statically element(p:item, p:Item)])
