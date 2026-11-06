(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:duplicate-context-a";
import module namespace b = "urn:import-regression:duplicate-context-b" at "duplicate-context-b.jq";
declare context item external;
declare context item external;
declare function a:f() { b:g() };
