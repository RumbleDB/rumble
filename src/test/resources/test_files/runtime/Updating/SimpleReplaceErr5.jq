(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := [1 to 4]
modify replace json value of $je[["foo"]] with "foo"
return $je

(: selector expr does not evaluate to Int :)