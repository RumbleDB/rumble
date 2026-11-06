(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:order-a";
import module namespace b = "urn:import-regression:order-b";
declare function a:f() { b:f() };
