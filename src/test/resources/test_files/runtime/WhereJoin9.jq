(:JIQS: ShouldRun; Output="({ "code" : "MI", "stores" : 2 }, { "code" : "CA", "stores" : 2 }, { "code" : "NY", "stores" : 1 }, { "code" : "MA", "stores" : 2 })" :)
let $stores := json-file("../../queries/stores.jsonl")
let $states := json-file("../../queries/states.jsonl")
return
for $store in $stores
for $state in $states
where $state.code eq $store.state
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }


