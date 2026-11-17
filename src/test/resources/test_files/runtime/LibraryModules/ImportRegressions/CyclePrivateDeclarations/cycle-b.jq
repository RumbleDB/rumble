(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:cycle-b";
import module namespace a = "urn:import-regression:cycle-a" at "cycle-a.jq";
declare %private variable $b:hidden := "b-hidden";
declare %private function b:secret($n) { a:f($n) };
declare function b:g($n) { if ($n le 0) then $b:hidden else b:secret($n - 1) };
