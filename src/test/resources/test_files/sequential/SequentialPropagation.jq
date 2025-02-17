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
                    switch ($l)
                                case "bar" return "foo";
                                case "foo" return "bar";
                                default return "none";
    } catch * {}