(:JIQS: ShouldRun; Output="({ "name" : "A", "stores" : 2 }, { "name" : "B", "stores" : 1 })" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" }, { "store number" : 2, "state" : "MA" }, { "store number" : 3, "state" : "MA" } ])
let $nations := parallelize([  { "state" : "A", "p" : 2 }, { "state" : "B", "p" : 1 } ])
return
  for $nation in $nations[], $store in $stores[][ position() eq $nation.p ] 
  return { "name": $nation.state, "stores": $store."store number" }
