(:JIQS: ShouldRun; Output="({ "name" : "MA", "stores" : [ { "store number" : 1, "state" : "MA" }, { "store number" : 2, "state" : "MA" } ] }, { "name" : "CA", "stores" : [ ] })" :)
let $stores := parallelize(({ "store number" : 1, "state" : "MA" }, { "store number" : 2, "state" : "MA" }))
let $nations := parallelize(({ "name": "US", "state": "MA" }, { "name": "US", "state": "CA" }))
return
  for $nation in $nations
  let $stores := $stores[ $$.state eq $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores ]
    }

(: Thanks Milos Nikolic for contributing the test :)
