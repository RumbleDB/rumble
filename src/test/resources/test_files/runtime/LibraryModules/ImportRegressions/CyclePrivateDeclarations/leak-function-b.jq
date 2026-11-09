(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:leak-function-b";
import module namespace a = "urn:import-regression:leak-function-a" at "leak-function-a.jq";
declare %private function b:secret() { "leaked" };
declare function b:g() { a:f#0 };
