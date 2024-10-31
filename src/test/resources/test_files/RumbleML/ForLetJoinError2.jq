(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:6:COLUMN:6:" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize([  { "name": "US", "state": "MA" }, { "name": "US", "state": "CA" } ])
return
  for $nation in $nations[]
  let $stores-for-nation := $stores[][ $$.state = $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores-for-nation."store number" ]
    }

(: Thanks Milos Nikolic for contributing the test :)
