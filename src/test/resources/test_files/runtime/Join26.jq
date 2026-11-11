(:JIQS: ShouldRun; Output="({ "id" : 1, "state" : "California" }, { "id" : 2, "state" : "Massachussetts" }, { "id" : 3, "state" : "Massachussetts" }, { "id" : 4, "state" : "California" }, { "id" : 5, "state" : "New York" }, { "id" : 6, "state" : "Michigan" }, { "id" : 7, "state" : "Michigan" })" :)
let $stores := parallelize((
  { "storeid" : 1, "code1" : "C", "code2" : "A"},
  { "storeid" : 2, "code1" : "M", "code2" : "A" },
  { "storeid" : 3, "code1" : "M", "code2" : "A" },
  { "storeid" : 4, "code1" : "C", "code2" : "A"},
  { "storeid" : 5, "code1" : "N", "code2" : "Y" },
  { "storeid" : 6, "code1" : "M", "code2" : "I" },
  { "storeid" : 7, "code1" : "M", "code2" : "I" }
))
let $states := parallelize((
  { "code1" : "C", "code2" : "A", "name" : "California" },
  { "code1" : "M", "code2" : "A", "name" : "Massachussetts" },
  { "code1" : "N", "code2" : "Y", "name" : "New York" },
  { "code1" : "M", "code2" : "I", "name" : "Michigan" },
  { "code1" : "W", "code2" : "A", "name" : "Washington" }
))
return
for $store in $stores
for $state in $states
where $state.code2 eq $store.code2 and $state.code1 eq $store.code1
order by $store.storeid
return { "id" : $store.storeid, "state" : $state.name }


