(:JIOS:ShouldCompile :)
while(true) {
    if (true) then {();} else {();}
    while (true) {}
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
                        rename json $l.a.foo as "bar";
                    else
                        1 + 1;
    } catch * {}
}