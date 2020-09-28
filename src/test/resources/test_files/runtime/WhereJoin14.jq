(:JIQS: ShouldRun; Output="({ "code" : "MI", "stores" : 2 }, { "code" : "CA", "stores" : 2 }, { "code" : "NY", "stores" : 1 }, { "code" : "MA", "stores" : 2 })" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize([  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ])
return
  for $nation in $nations[], $store at $pos in $stores[]
  where $store.state eq $nation.state
  return { "stores": $store."store number" }

(: Thanks Milos Nikolic for contributing the test :)
