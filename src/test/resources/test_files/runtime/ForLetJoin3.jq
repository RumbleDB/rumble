(:JIQS: ShouldRun; Output="" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize(())
return
  for $nation in $nations
  let $stores-for-nation := $stores[][ $$.state eq $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores-for-nation ]
    }

(: Thanks Milos Nikolic for contributing the test :)
