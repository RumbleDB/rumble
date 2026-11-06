(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace a = "urn:import-regression:hidden-user";
import module namespace h = "urn:import-regression:hidden" at "hidden.jq";
declare function a:f() { h:f() };
