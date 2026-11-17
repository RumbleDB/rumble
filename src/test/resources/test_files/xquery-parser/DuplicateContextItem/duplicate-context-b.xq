(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:duplicate-context-b";
import module namespace a = "urn:import-regression:duplicate-context-a" at "duplicate-context-a.xq";
declare function b:g() { 1 };
