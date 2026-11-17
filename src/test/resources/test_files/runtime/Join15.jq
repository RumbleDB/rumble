(:JIQS: ShouldRun; Output="({ "code" : "CA", "stores" : 2 }, { "code" : "MA", "stores" : 2 }, { "code" : "MI", "stores" : 2 }, { "code" : "NY", "stores" : 1 })" :)
let $stores := json-lines("../../queries/stores.jsonl")
let $states := json-lines("../../queries/states.jsonl")
return
for $store in $stores
for $state in $states[$$.code eq $store.state]
group by $code := $state.code
order by $code
return { "code" : $code, "stores" : count($store) }


