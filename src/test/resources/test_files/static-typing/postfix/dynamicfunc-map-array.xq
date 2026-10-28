(:JIQS: ShouldRun; Output="(1, 2)" :)
(: Calling a map or an array selects like a lookup: a map's value for a key, which may be absent, or an
   array's member. :)
declare function local:value($m as map(xs:string, xs:integer)) as xs:integer? { $m("a") };
declare function local:member($a as array(xs:integer)) as xs:integer { $a(2) };
(local:value(map { "a": 1 }), local:member(array { 1, 2 }))
