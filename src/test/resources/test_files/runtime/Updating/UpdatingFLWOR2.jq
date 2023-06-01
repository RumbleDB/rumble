(:JIQS: ShouldRun; Output="({ "a" : { "bar" : 1 } }, { "b" : { "foo" : 2 } }, { "a" : { "bar" : 1 } }, { "b" : { "foo" : 2 } })" :)
copy $je := for $i in (1 to 4)
                 return
                    if($i mod 2 eq 0)
                    then
                        {"b" : {"foo" : 1}}
                    else
                        {"a" : {"foo" : 1}}
modify
    for $l in $je
    return
        if($l.a)
        then
            rename $l.a.foo as "bar"
        else
            replace value of $l.b.foo with 2
return $je