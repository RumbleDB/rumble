(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
import schema namespace p = "urn:path-steps" at "PathSteps.xsd";

(: p:meta allows ##other elements, which excludes the target namespace, so p:meta has no p:x child. :)
declare function local:other($order as schema-element(p:order)) {
    $order/p:meta/p:x
};
1
