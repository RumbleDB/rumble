(:JIQS: ShouldRun; Output="({ "name" : "MA", "stores" : 1 }, { "name" : "CA", "stores" : null })" :)
variable $res := ();
let $stores := [  { "store number" : 1, "state" : "MA" } ]
let $nations := [  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ]
for $nation in $nations[], $store allowing empty in $stores[]
    [ $$.state eq $nation.state ]
return $res := ($res, { "name": $nation.state, "stores": $store."store number" });
$res

(: Thanks Milos Nikolic for contributing the test :)