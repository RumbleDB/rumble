(:JIQS: ShouldCrash; ErrorCode="JNUP0008"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $je := false
modify append json 5 into $je
return $je

(: target expr does not evaluate to array :)