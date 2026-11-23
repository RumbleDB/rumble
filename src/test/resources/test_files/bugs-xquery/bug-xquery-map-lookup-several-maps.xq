(:JIQS: ShouldCrash; ErrorCode="XPTY0004":)
(: A lookup on several maps returns a value from each of them. :)
declare function local:f($x as xs:integer) { $x };
declare function local:g($m as map(xs:string, xs:integer)*) { local:f($m?a) };
local:g((map{"a": 1}, map{"a": 2}))
