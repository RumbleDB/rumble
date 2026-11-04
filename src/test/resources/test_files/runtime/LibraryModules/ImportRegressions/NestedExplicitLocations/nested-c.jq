(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace c = "urn:import-regression:c";
import module namespace b = "urn:import-regression:b" at "nested-b2.jq";
declare function c:f() { b:two() };
