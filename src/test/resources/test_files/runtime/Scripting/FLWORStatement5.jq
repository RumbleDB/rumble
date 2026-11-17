(:JIQS: ShouldRun; Output="[ [ ], { "name" : "MA", "stores" : 1 }, { "name" : "CA", "stores" : null } ]" :)
variable $res := [];
let $stores :=[  { "store number" : 1, "state" : "MA" } ]
let $nations := [  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ]
let $join :=
    for $nation in $nations[], $store allowing empty in $stores[]
        [ $$.state eq $nation.state ]
    return { "name": $nation.state, "stores": $store."store number" }
return $res := [$res, $join];
$res

(: Thanks Milos Nikolic for contributing the test :)