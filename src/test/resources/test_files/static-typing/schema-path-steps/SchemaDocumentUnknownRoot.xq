(:JIQS: ShouldCrash; ErrorCode="XPST0005" :)
import schema namespace l = "urn:library" at "Library.xsd";

(: The function is never called, so only static typing can report the error. :)
declare function local:books($library as document-node(schema-element(l:library))) {
    $library/l:book
};
1
