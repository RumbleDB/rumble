(:JIQS: ShouldCrash; ErrorCode="JNUP0007"; ErrorMetadata="LINE:3:COLUMN:7:" :)
copy $users := []
modify insert {"userID" : "U07", "name" : "Annabel Lee"} into $users
return $users[]

(: no position index specified :)