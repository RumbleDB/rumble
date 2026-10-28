(:JIQS: ShouldCrash; ErrorCode="XPTY0004":)
(: A key may be absent from a map, so $m?b may be empty and must not satisfy xs:integer statically. :)
declare function local:f($x as xs:integer) { $x };
declare function local:g($m as map(xs:string, xs:integer)) { local:f($m?b) };
local:g(map{"a": 1})
