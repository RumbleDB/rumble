(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
import schema namespace d = "urn:sections" at "Sections.xsd";

(: The function is never called, so only static typing can report the error. :)
declare function local:missing($doc as schema-element(d:doc)) {
    $doc//d:missing
};
1
