(:JIQS: ShouldCrash; ErrorCode="JNUP0009"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy json $je := { "a" : 1, "b" : 2, "c" : 3 }
modify (replace json value of $je.foo with "foo", replace json value of $je.foo with "bar")
return $je

(: replace at same object with same selector :)