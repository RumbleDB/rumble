(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace u = "urn:import-regression:scope-uses-type";
declare namespace s = "urn:import-regression:scope-schema";
declare function u:cast() { 5 cast as s:T };
