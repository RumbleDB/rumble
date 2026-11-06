(:JIQS: ShouldRun; Output="({ "name" : "MA", "stores" : [ 1 ] }, { "name" : "CA", "stores" : [ ] })" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize([  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ])
return
  for $nation in $nations[]
  let $stores-for-nation := $stores[][ $$.state eq $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores-for-nation."store number" ]
    }

(: Thanks Milos Nikolic for contributing the test :)
