(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify delete $je[["foo"]]
return $je

(: selector expr does not evaluate to int :)