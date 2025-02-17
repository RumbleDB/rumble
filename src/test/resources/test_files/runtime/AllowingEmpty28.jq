(:JIQS: ShouldRun; Output="({ "name" : "MA", "stores" : 1 }, { "name" : "CA", "stores" : null })" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := [  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ]
return
for $nation in $nations[], $store allowing empty in $stores[]
    [ $$.state eq $nation.state ] 
return { "name": $nation.state, "stores": $store."store number" }

(: Thanks Milos Nikolic for contributing the test :)
