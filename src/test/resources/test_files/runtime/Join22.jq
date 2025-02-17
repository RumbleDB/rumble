(:JIQS: ShouldRun; Output="{ "code" : null, "stores" : 0 }" :)
let $stores := parallelize(())
let $states := parallelize(())
return
for $state allowing empty in $states
for $store allowing empty in $stores[$$.state eq $state.code]
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }

