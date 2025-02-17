(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1}
modify replace value of json $je.$je with "foo"
return $je

(: selector expr does not evaluate to String :)