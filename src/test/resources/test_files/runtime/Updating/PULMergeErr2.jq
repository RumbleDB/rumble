(:JIQS: ShouldCrash; ErrorCode="JNUP0009"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify (replace value of json $je.a with "foo", replace json value of $je.a with "bar")
return $je

(: replace at same object with same selector :)