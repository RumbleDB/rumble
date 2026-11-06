(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace b = "urn:import-regression:load-b";
import module namespace a = "urn:import-regression:load-a" at "load-cycle-a.xq";
declare variable $b:vb := 2;
declare %private variable $b:hidden := 3;
declare function b:f() { a:g#0 };
