(:JIQS: ShouldRun; Output="" :)
let $stores := parallelize(())
let $states := parallelize(())
return
for $state in $states
for $store allowing empty in $stores[$$.state eq $state.code]
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }

