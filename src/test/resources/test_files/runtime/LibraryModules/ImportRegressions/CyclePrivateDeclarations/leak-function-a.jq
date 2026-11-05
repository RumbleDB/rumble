(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:leak-function-a";
import module namespace b = "urn:import-regression:leak-function-b" at "leak-function-b.jq";
declare function a:f() { b:secret() };
