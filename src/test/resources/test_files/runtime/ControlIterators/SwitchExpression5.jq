(:JIQS: ShouldRun; Output="({ "v" : 1, "switch" : "foo" }, { "v" : 2, "switch" : "foo" }, { "v" : 3, "switch" : "foo" }, { "v" : 4, "switch" : null }, { "v" : 5, "switch" : null }, { "v" : 6, "switch" : [ "baz", "barz2" ] }, { "v" : 7, "switch" : [ "baz", "barz2" ] }, { "v" : 8, "switch" : [ "baz", "barz2" ] }, { "v" : 9, "switch" : [ "baz", "barz2" ] }, { "v" : 10, "switch" : [ "baz", "barz2" ] })" :)
for $v in (1 to 10)
return
    {"v": $v, "switch":
        switch (true)
        case (4 > $v) return "foo"
        case (6 > $v) return ()
        default return ("baz", "barz2")}