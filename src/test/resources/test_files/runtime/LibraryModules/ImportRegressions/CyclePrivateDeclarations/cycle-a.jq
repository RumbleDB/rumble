(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:cycle-a";
import module namespace b = "urn:import-regression:cycle-b" at "cycle-b.jq";
declare %private variable $a:hidden := "a-hidden";
declare %private function a:secret() { "a-secret" };
declare function a:f($n) { if ($n le 0) then a:secret() else b:g($n - 1) };
