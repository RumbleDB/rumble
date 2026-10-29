(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:cycle-b";
import module namespace a = "urn:cycle-a" at "a.jq";
declare function b:answer() { a:base() };
