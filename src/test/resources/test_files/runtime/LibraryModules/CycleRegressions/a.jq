(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:cycle-a";
import module namespace b = "urn:cycle-b" at "b.jq";
declare function a:answer() { b:answer() };
declare function a:base() { 42 };
