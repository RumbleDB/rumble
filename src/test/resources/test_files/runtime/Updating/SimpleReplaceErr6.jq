(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := {"a" : 1}
modify replace json value of $je.$je with "foo"
return $je

(: selector expr does not evaluate to String :)