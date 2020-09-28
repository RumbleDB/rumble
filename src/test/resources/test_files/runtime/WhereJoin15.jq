(:JIQS: ShouldRun; Output="({ "code" : "MI", "stores" : 2 }, { "code" : "CA", "stores" : 2 }, { "code" : "NY", "stores" : 1 }, { "code" : "MA", "stores" : 2 })" :)
let $stores := parallelize((
  { "storeid" : 1, "state" : "CA" },
  { "storeid" : 2, "state" : "MA" },
  { "storeid" : 3, "state" : "MA" },
  { "storeid" : 4, "state" : "CA" },
  { "storeid" : 5, "state" : "NY" },
  { "storeid" : 6, "state" : "MI" },
  { "storeid" : 7, "state" : "MI" }
))
let $states := parallelize(())
return
for $store in $stores
for $state allowing empty in $states
where $state.code eq $store.state
return { "id" : $store.storeid, "state" : $state.name }


