(:JIQS: ShouldNotCompile; ErrorCode="XQST0052" :)
(: A library does not see the schema definitions of another library. :)
import module namespace w = "urn:import-regression:scope-with-schema" at "with-schema.xq";
import module namespace u = "urn:import-regression:scope-uses-type" at "uses-type.xq";
u:cast()
