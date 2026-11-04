(:JIQS: ShouldNotCompile; ErrorCode="XQST0052" :)
(: A library does not see the schema definitions of the module importing it. :)
import schema namespace s = "urn:import-regression:scope-schema" at "scope.xsd";
import module namespace u = "urn:import-regression:scope-uses-type" at "uses-type.xq";
u:cast()
