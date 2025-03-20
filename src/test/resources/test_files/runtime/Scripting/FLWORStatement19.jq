(:JIQS: ShouldRun; Output="({ "code" : "CA", "stores" : 2, "counter" : 0 }, { "code" : "CA", "stores" : 2, "counter" : 1 }, { "code" : "CA", "stores" : 2, "counter" : 2 }, { "code" : "CA", "stores" : 2, "counter" : 3 }, { "code" : "CA", "stores" : 2, "counter" : 4 }, { "code" : "MA", "stores" : 2, "counter" : 0 }, { "code" : "MA", "stores" : 2, "counter" : 1 }, { "code" : "MA", "stores" : 2, "counter" : 2 }, { "code" : "MA", "stores" : 2, "counter" : 3 }, { "code" : "MA", "stores" : 2, "counter" : 4 }, { "code" : "MI", "stores" : 2, "counter" : 0 }, { "code" : "MI", "stores" : 2, "counter" : 1 }, { "code" : "MI", "stores" : 2, "counter" : 2 }, { "code" : "MI", "stores" : 2, "counter" : 3 }, { "code" : "MI", "stores" : 2, "counter" : 4 }, { "code" : "NY", "stores" : 1, "counter" : 0 }, { "code" : "NY", "stores" : 1, "counter" : 1 }, { "code" : "NY", "stores" : 1, "counter" : 2 }, { "code" : "NY", "stores" : 1, "counter" : 3 }, { "code" : "NY", "stores" : 1, "counter" : 4 })" :)
variable $res := ();
let $stores := json-lines("../../../queries/stores.jsonl")
let $states := json-lines("../../../queries/states.jsonl")
return
for $store in $stores
for $state in $states
where $state.code eq $store.state
group by $code := $state.code
order by $code
return {
    variable $counter := 0;
    while (true) {
        $res := ($res, { "code" : $code, "stores" : count($store), "counter": $counter });
        $counter := $counter + 1;
        if ($counter eq count($states)) then break loop;
        else ();
    }
}
$res