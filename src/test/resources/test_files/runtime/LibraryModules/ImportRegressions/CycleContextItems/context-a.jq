(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:context-a";
import module namespace b = "urn:import-regression:context-b" at "context-b.jq";
declare context item external;
declare function a:f() { b:g() + 1 };
