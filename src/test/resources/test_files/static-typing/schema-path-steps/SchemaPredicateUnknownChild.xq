(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

(: The function is never called, so only static typing can report the error. :)
declare function local:expensive($order as schema-element(p:order)) {
    $order/p:item[p:prices > 1]
};
1
