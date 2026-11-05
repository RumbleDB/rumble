(:JIQS: ShouldNotCompile; ErrorCode="XQST0099" :)
(: Modules of a cycle may each declare the context item, but only once each. :)
import module namespace a = "urn:import-regression:duplicate-context-a" at "duplicate-context-a.jq";
declare context item := 5;
a:f()
