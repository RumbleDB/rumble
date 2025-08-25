(:JIQS: ShouldRun; Output="({ "code" : "CA", "stores" : 2 }, { "code" : "MA", "stores" : 2 }, { "code" : "MI", "stores" : 2 }, { "code" : "NY", "stores" : 1 })" :)
variable $res := ();
let $stores := json-lines("../../../queries/stores.jsonl")
let $states := json-lines("../../../queries/states.jsonl")
return
for $store in $stores
for $state in $states
where $state.code eq $store.state
group by $code := $state.code
order by $code
return $res := ($res, { "code" : $code, "stores" : count($store) });
$res