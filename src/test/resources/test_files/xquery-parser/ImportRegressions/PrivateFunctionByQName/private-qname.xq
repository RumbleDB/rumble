(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace m = "urn:import-regression:private-qname";
declare %private function m:p() { "found" };
declare function m:f() {
  fn:function-lookup(xs:QName("m:p"), 0)(),
  fn:function-name(m:p#0) eq xs:QName("m:p")
};
