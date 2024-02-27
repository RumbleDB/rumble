(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy json $users := []
modify insert json {"userID" : "U07", "name" : "Annabel Lee"} into $users
return $users[]