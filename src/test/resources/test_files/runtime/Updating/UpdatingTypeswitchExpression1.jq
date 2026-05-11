(:JIQS: ShouldRun; Output="{ "b" : 2, "c" : 3, "a" : 4 }" :)
copy $je := { "a" : 1, "b" : 2, "c" : 3 }
modify
    typeswitch($je.a)
    case boolean+ return replace value of json $je.a with false
    case decimal return replace value of json $je.a with 4
    case string* return replace value of json $je.a with "not this"
    default return ()
return $je