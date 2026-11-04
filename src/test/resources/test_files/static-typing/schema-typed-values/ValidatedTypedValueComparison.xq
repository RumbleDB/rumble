(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:typed-values" at "TypedValues.xsd";

(: The function is never called, so only static typing can report the error. :)
declare function local:compare() {
    validate { <t:price>4.5</t:price> } eq "4.5"
};
1
