(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:6:COLUMN:2:" :)
let $stores := parallelize(())
let $states := parallelize(())
return
for $state allowing empty in $states
for $store allowing empty in $stores
where $store.state eq $state.code
group by $code := $state.code
return { "code" : $code, "stores" : count($store) }

