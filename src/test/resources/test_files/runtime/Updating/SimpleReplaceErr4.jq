(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := [1 to 4]
modify replace value of json false[[1]] with "foo"
return $je

(: target expr does not evaluate to single array or object :)