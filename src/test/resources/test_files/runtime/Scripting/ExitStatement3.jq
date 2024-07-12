(:JIQS: ShouldRun; Output="({ "b" : 1 }, { "b" : 2 }, { "b" : 3 }, { "b" : 4 })" :)
while (true) {
    exit returning for $a in ( {"a" : 1}, {"a" : 2}, {"a" : 3}, {"a" : 4} )
                   return
                       copy $je := $a
                       modify rename json $je.a as "b"
                       return $je;
}