(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace c = "urn:import-regression:duplicate-context-library";
declare context item external;
declare context item external;
declare function c:f() { 1 };
