(:JIQS: ShouldRun; Output="(2, 1)" :)
declare function local:size($m as map(xs:string, xs:integer)) as xs:integer { map:size($m) };
declare function local:first($m as map(xs:integer, xs:string*)) as xs:string? { $m?1[1] };
(
    local:size(map { "a": 1, "b": 2 }),
    local:first(map { 1: ("1", "x"), 2: () })
)
