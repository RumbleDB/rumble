(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace my-module = "moduleimportingmodule.jq";
import module namespace im = "module.jq";

declare variable $my-module:x := 3 + $im:x;

declare function my-module:func() {
  im:main($my-module:x) + 1
};
