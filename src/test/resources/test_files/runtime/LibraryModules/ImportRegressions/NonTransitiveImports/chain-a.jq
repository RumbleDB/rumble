(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:chain-a";
import module namespace b = "urn:import-regression:chain-b" at "chain-b.jq";
declare function a:f() { b:f() };
