(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $je := {"a" : 1}
modify delete $je.$je
return $je

(: target expr does not evaluate to string :)