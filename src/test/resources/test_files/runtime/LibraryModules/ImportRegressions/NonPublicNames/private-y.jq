(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace m = "urn:import-regression:same-private";
declare %private variable $m:p := "y-var";
declare %private function m:p() { "y-fn" };
declare function m:y() { ($m:p, m:p()) };
