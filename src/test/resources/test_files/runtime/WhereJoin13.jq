(:JIQS: ShouldRun; Output="" :)
let $stores := parallelize(())
let $states := parallelize(())
return
for $state allowing empty in $states
for $store in $stores
where $store.state eq $state.code
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }

