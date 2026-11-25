(:JIQS: ShouldRun; Output="(1, 2, 3, 2)" :)
(: The map functions keep the key and value types of the maps they build. :)
declare function local:f($m as map(xs:string, xs:integer)) as xs:integer? { $m?a };
declare function local:g($m as map(xs:string, xs:integer)) as xs:integer? { map:get($m, "b") };
declare function local:h($m as map(xs:anyAtomicType, xs:integer+)) as xs:integer { count(map:keys($m)) };
(
  local:f(map:entry("a", 1)),
  local:g(map { "b": 2 }),
  local:f(map:remove(map { "a": 3, "b": 4 }, "b")),
  local:h(map:put(map { "a": (1, 2) }, 3, 5))
)
