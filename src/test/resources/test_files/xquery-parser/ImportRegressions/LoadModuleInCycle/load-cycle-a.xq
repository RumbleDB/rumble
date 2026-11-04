(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:load-a";
import module namespace b = "urn:import-regression:load-b" at "load-cycle-b.xq";
declare variable $a:va := 1;
declare function a:g() { b:f() };
