(:JIQS: ShouldRun; Output="({ "code" : "MI", "stores" : 2 }, { "code" : "CA", "stores" : 2 }, { "code" : "NY", "stores" : 1 }, { "code" : "MA", "stores" : 2 })" :)
let $stores := parallelize(())
let $states := parallelize(())
return
for $state allowing empty in $states
for $store allowing empty in $stores
where $store.state eq $state.code
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }

