(:JIQS: ShouldCrash; ErrorCode="JNUP0005"; ErrorMetadata="LINE:3:COLUMN:8:" :)
copy json $je := { "a" : 1, "b" : 2, "c" : 3 }
modify (insert json "foobar": "barfoo" into $je, insert json "foobar": "barfoo" into $je)
return $je

(: insert same key into same object with 2 inserts :)