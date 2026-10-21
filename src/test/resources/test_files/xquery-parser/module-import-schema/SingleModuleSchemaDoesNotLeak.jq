(:JIQS: ShouldNotCompile; ErrorCode="XPST0051" :)
xquery version "3.1";
(: A module import does not import its schema definitions. :)
import module namespace m = "urn:schema-isolation" at "b-schema.xqm";
declare namespace t = "urn:module-local-types";
1 instance of t:Code
