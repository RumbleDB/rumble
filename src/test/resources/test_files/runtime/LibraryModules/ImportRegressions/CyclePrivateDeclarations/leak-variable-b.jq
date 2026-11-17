(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:leak-variable-b";
import module namespace a = "urn:import-regression:leak-variable-a" at "leak-variable-a.jq";
declare %private variable $b:secret := "leaked";
declare function b:g() { a:f#0 };
