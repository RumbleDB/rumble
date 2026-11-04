(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:triangle-a";
import module namespace b = "urn:import-regression:triangle-b" at "triangle-b.jq";
declare namespace c = "urn:import-regression:triangle-c";
declare function a:f() { c:f() };
