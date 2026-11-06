(:JIQS: ShouldRun; Output="({ "name" : "MA", "stores" : [ { "store number" : 1, "state" : "MA" }, { "store number" : 2, "state" : "MA" } ] }, { "name" : "CA", "stores" : [ ] })" :)
let $stores := parallelize(({ "store number" : 1, "state" : "MA" }, { "store number" : 2, "state" : "MA" }))
let $nations := parallelize(({ "name": "US", "state": "MA" }, { "name": "US", "state": "CA" }))
return
  for $nation in $nations
  let $stores-for-nation := $stores[ $$.state eq $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores-for-nation ]
    }

(: Thanks Milos Nikolic for contributing the test :)
