(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
import schema namespace t = "urn:arguments" at "Arguments.xsd";
declare function local:accept($value as t:NumberOrBoolean) { $value };

(: Function conversion casts untypedAtomic input, but must not cast an already typed string to integer. :)
local:accept(xs:string("12"))
