(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: math:sin of an empty sequence is empty, so the result of an xs:double? may be empty. :)
declare function local:f($x as xs:double?) as xs:double { math:sin($x) };
local:f(1e0)
