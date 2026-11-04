(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace f = "urn:import-regression:non-public-flwor";
declare %private variable $f:x := 10;
declare function f:a() {
  for $i in parallelize(1 to 2)
  let $y := $f:x
  return $i + $y
};
