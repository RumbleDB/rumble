(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
copy json $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    switch($je.a)
    case 1 return replace json value of $je.a with false
    case 2 return replace json value of $je.a with 4
    case 3 return replace json value of $je.a with "not this"
    default return ()
return $je

(: switch cannot house updating exprs :)