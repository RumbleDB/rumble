(:JIQS: ShouldParse :)
for $v in (1 to 10)
return
    {"v": $v, "switch":
        switch (true)
        case (4 > $v) return "foo"
        case (6 > $v) return ()
        default return ("baz", "barz2")}