(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace m = "urn:import-regression:same-private";
declare function m:x() { ($m:p, m:p(), let $m:p := "local" return $m:p) };
declare %private variable $m:p := "x-var";
declare %private function m:p() { "x-fn" };
