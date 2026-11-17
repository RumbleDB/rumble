(:JIQS: ShouldRun; Output="({ "id" : 1, "state" : "California" }, { "id" : 2, "state" : "Massachussetts" }, { "id" : 3, "state" : "Massachussetts" }, { "id" : 4, "state" : "California" }, { "id" : 5, "state" : "New York" }, { "id" : 6, "state" : "Michigan" }, { "id" : 7, "state" : "Michigan" })" :)
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
for $state in $states[$$.code eq $store.state]
order by $store.storeid
return { "id" : $store.storeid, "state" : $state.name }


