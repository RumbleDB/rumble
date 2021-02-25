(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:0:COLUMN:0:" :)
module namespace math = "math.jq";

declare function math:main($y) {
   math:func($math:x) + 2
};
declare variable $math:x := 2;

declare function math:func($y) {
   $y + 4
};
