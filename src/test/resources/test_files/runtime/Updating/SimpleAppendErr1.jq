(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := { "a" : 1, "b" : 2, "c" : 3 }
modify append json 5 into false
return $je

(: target expr does not evaluate to array :)