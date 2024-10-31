(:JIQS: ShouldRun; Output="" :)
let $stores := parallelize([  { "store number" : 1, "state" : "MA" } ])
let $nations := parallelize(())
return
  for $nation in $nations
  let $stores := $stores[][ $$.state eq $nation.state ] 
  return
    {
      "name": $nation.state,
      "stores": [ $stores ]
    }

(: Thanks Milos Nikolic for contributing the test :)
