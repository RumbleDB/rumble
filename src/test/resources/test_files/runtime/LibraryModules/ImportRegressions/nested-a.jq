(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:a";
import module namespace b = "urn:import-regression:b" at "nested-b1.jq";
import module namespace c = "urn:import-regression:c" at "nested-c.jq";
declare function a:f() { b:one() + c:f() };
