(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    typeswitch($je.a)
    case boolean+ return replace value of $je.a with false
    case decimal return 4
    case string* return replace value of $je.a with "not this"
    default return ()
return $je

(: at least one branch expr is not simple :)