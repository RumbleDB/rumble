(:JIQS: ShouldRun; Output="{ "name" : "MA", "stores" : 1 }" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize([  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ])
return
  for $nation in $nations[], $store in $stores[]
  where $store.state eq $nation.state 
  return { "name": $nation.state, "stores": $store."store number" }

(: Thanks Milos Nikolic for contributing the test :)
