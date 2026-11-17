(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

(: The function is never called, so only static typing can report the error. :)
declare function local:missing($order as schema-element(p:order)) {
    $order/p:missing
};
1
