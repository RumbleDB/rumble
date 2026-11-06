(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace c = "urn:import-regression:triangle-c";
import module namespace a = "urn:import-regression:triangle-a" at "triangle-a.jq";
declare function c:f() { "c-fn" };
