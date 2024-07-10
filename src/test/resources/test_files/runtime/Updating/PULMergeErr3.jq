(:JIQS: ShouldCrash; ErrorCode="JNUP0009"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy $je := [1 to 4]
modify (replace value of json $je[[1]] with "foo", replace value of json $je[[1]] with "bar")
return $je

(: replace at same array with same selector :)