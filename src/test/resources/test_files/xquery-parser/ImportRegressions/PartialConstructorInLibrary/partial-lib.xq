(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace m = "urn:import-regression:partial";
import schema namespace s = "urn:import-regression:partial-schema" at "partial.xsd";
declare function m:f() { s:myint(?)(5) };
