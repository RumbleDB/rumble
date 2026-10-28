(:JIQS: ShouldRun; Output="(1, 2, 3, 2)" :)
(: The array functions keep the member type of their array. :)
declare function local:member($m as xs:integer) as xs:integer { $m };
declare function local:size($a as array(xs:integer)) as xs:integer { array:size($a) };
(
    local:member(array:get([1, 2], 1)),
    local:member(array:head(array:reverse([1, 2]))),
    local:size(array:append(array:tail([0, 1, 2]), 3)),
    local:size(array:filter([1, 2], function($x) { true() }))
)
