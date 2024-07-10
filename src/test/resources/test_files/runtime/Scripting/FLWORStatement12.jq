(:JIQS: ShouldRun; Output="" :)
variable $res := ();
let $stores := parallelize((
  { "storeid" : 1, "state" : "CA" },
  { "storeid" : 2, "state" : "MA" },
  { "storeid" : 3, "state" : "MA" },
  { "storeid" : 4, "state" : "CA" },
  { "storeid" : 5, "state" : "NY" },
  { "storeid" : 6, "state" : "MI" },
  { "storeid" : 7, "state" : "MI" }
))
let $states := parallelize((
  { "code" : "CA", "name" : "California" },
  { "code" : "MA", "name" : "Massachussetts" },
  { "code" : "NY", "name" : "New York" },
  { "code" : "MI", "name" : "Michigan" },
  { "code" : "WA", "name" : "Washington" }
))
return
    for $store in $stores
    for $state in $states
    where $state.code eq $store.state
    group by $code := $state.code
    order by $code
    return {continue loop; $res := ($res, { "code" : $code, "stores" : count($store) });}
$res