(:JIQS: ShouldNotCompile; ErrorCode="XQST0099" :)
(: A library cannot contain more than one context item declaration either. :)
import module namespace c = "urn:import-regression:duplicate-context-library" at "duplicate-context-library.jq";
declare context item := 5;
c:f()
