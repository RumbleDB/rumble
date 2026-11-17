(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace x = "urn:import-regression:fallback-user";
import module namespace n = "urn:import-regression:fallback" at "fallback-bad.jq";
declare function x:g() { n:f() };
