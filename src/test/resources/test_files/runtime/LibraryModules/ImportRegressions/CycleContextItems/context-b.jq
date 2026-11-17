(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:context-b";
import module namespace a = "urn:import-regression:context-a" at "context-a.jq";
declare context item external;
declare function b:g() { 1 };
