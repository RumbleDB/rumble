(:JIQS: ShouldCrash; ErrorCode="XPTY0004":)
(: A wildcard lookup returns every value of the map. :)
declare function local:f($x as xs:integer) { $x };
declare function local:g($m as map(xs:string, xs:integer)) { local:f($m?*) };
local:g(map{"a": 1, "b": 2})
