(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: A key may be absent from a map, so $m.b may be empty and must not satisfy integer statically. :)
declare function local:f($x as integer) { $x };
declare function local:g($m as map(string, integer)) { local:f($m.b) };
local:g(map{"a": 1})
