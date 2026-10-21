(:JIQS: ShouldNotCompile; ErrorCode="XPST0051" :)
xquery version "3.1";
(: Combining same-namespace modules must not export their schemas to the caller. :)
import module namespace m = "urn:schema-isolation" at "a-empty.xqm", "b-schema.xqm";
declare namespace t = "urn:module-local-types";
1 instance of t:Code
