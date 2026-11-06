(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    switch($je.a)
    case 1 return replace value of json $je.a with false
    case 2 return replace value of json $je.a with 4
    case 3 return replace value of json $je.a with "not this"
    default return ()
return $je

(: switch cannot house updating exprs :)