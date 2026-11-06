(:JIQS: ShouldCrash; ErrorCode="RBST0002"; ErrorMetadata="LINE:5:COLUMN:29:" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize([  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ])
return
  for $nation in $nations[], $store at $pos in $stores[][ $$.state eq $nation.state ] 
  return { "stores": $store."store number" }

(: Thanks Milos Nikolic for contributing the test :)
