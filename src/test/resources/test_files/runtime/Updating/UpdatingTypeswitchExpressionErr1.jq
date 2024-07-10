(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:4:COLUMN:4:" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    typeswitch(replace value of json $je.a with 4)
    case boolean+ return replace value of json $je.a with false
    case decimal return replace value of json $je.a with 4
    case string* return replace value of json $je.a with "not this"
    default return ()
return $je

(: typeswitch condition expr is not simple :)