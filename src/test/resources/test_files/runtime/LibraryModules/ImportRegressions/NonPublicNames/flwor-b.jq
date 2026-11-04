(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:2:COLUMN:0:" :)
module namespace f = "urn:import-regression:non-public-flwor";
declare %private variable $f:x := 20;
declare function f:b() {
  for $i in parallelize(1 to 2)
  return $i + $f:x
};
declare function f:shadowed() {
  for $f:x in 1 to 2
  return $f:x + 1
};
