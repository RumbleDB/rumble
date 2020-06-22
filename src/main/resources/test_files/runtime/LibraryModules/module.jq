(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:0:COLUMN:0:" :)
module namespace my-module = "module.jq";

declare variable $my-module:x := 2;

declare function my-module:func($y) {
   $y + 4
};
