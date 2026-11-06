(:JIQS: ShouldRun; Output="{ "id" : null, "state" : null }" :)
let $stores := parallelize(())
let $states := parallelize((
  { "code" : "CA", "name" : "California" },
  { "code" : "MA", "name" : "Massachussetts" },
  { "code" : "NY", "name" : "New York" },
  { "code" : "MI", "name" : "Michigan" },
  { "code" : "WA", "name" : "Washington" }
))
return
for $store allowing empty in $stores
for $state allowing empty in $states[$$.code eq $store.state]
return { "id" : $store.storeid, "state" : $state.name }


