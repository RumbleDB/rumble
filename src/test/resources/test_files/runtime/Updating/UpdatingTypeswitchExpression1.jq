(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "a" : 4 }" :)
copy json $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    typeswitch($je.a)
    case boolean+ return replace json value of $je.a with false
    case decimal return replace json value of $je.a with 4
    case string* return replace json value of $je.a with "not this"
    default return ()
return $je