(:JIQS: ShouldRun; Output="({ "str" : "P4Y5MT5M" }, { "str" : "asdffwesdafafasdf" })" :)
for $i in (
{"str": 1},
{"str": 2},
{"str": duration("P4Y5MT5M")},
{"str":  3.5},
{"str":  "asdffwesdafafasdf"},
{"str":  "cd"}
)
where typeswitch($i.str)
    case integer | decimal return parallelize(for $i in 1 to 1000 return 1 eq 2)[500]
    case $a as string return parallelize(for $i in 1 to 1000 return ends-with(($a cast as string), "df"))[500]
    default return parallelize(for $i in 1 to 1000 return 1+1 eq 2)[500]
return $i

(: case with for and variables :)
