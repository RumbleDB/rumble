(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace n = "urn:import-regression:fallback";
import module namespace x = "urn:import-regression:fallback-user" at "fallback-user.jq";
import module namespace m = "urn:import-regression:missing" at "does-not-exist.jq";
declare function n:f() { "bad" };
