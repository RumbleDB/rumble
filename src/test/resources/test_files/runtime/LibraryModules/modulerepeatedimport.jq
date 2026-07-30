(:JIQS: ShouldNotParse; ErrorCode="XPST0003"; ErrorMetadata="LINE:1:COLUMN:0:" :)
module namespace my-module = "modulerepeatedimport.jq";
import module namespace mod1 = "moduleimportingmodule.jq";
import module namespace mod2 = "module.jq";

declare function my-module:func() {
  mod1:func() + mod2:func(7)
};
