(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace my-module = "modulewithmismatchingvariablens.jq";

declare function my-module:main($y) {
   my-module:func($my-module:x) + 2
};
declare variable $x := 2;

declare function my-module:func($y) {
   $y + 4
};
