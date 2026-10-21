(:JIQS: ShouldNotCompile; ErrorCode="XPST0051" :)
xquery version "3.1";
(: A shared target namespace does not share schema imports between library modules. :)
import module namespace m = "urn:schema-isolation" at "b-schema.xqm", "c-uses-schema.xqm";
exists(m:use#1)
