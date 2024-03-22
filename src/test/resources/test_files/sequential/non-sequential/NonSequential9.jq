(:JIQS: ShouldCompile :)
try {
        variable $je := for $i in (1 to 4)
                             return
                                if($i mod 2 eq 0)
                                then
                                    {"b" : {"foo" : 1}}
                                else
                                    {"a" : {"foo" : 1}};
        for $l in $je
                return
                    if($l.a) then
                        2 + 1;
                    else
                        1 + 1;
    } catch * {}