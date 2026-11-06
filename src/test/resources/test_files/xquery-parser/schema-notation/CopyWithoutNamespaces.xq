(:JIQS: ShouldCrash; ErrorCode="XQTY0086" :)
import schema namespace n = "urn:notation" at "Notation.xsd";
(: Preserving a NOTATION annotation requires retaining the namespaces used by its typed value. :)
declare construction preserve;
declare copy-namespaces no-preserve, inherit;
let $node := validate strict { <n:image>n:jpeg</n:image> }
return <copy>{ $node }</copy>
