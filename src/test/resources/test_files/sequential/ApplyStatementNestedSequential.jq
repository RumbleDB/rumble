(:JIQS: ShouldCompile :)
{
    variable $je := for $i in (1 to 4)
                     return
                        if($i mod 2 eq 0)
                        then
                            {"b" : {"foo" : 1}}
                        else
                            {"a" : {"foo" : 1}};
        for $l in $je
        return
            switch (())
            case "bar" return rename json $l.a.foo as "bar";
            case "foo" return variable $res := "foo";
            default return variable $res := "none";
}