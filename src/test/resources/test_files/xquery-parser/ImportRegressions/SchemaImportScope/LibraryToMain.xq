(:JIQS: ShouldNotCompile; ErrorCode="XQST0052" :)
(: A module import does not bring in the schema definitions of the imported module. :)
import module namespace w = "urn:import-regression:scope-with-schema" at "with-schema.xq";
declare namespace s = "urn:import-regression:scope-schema";
5 cast as s:T
