(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace m = "urn:import-regression:hidden-functions";
declare function m:caller() { m:callee() };
