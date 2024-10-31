(:JIQS: ShouldRun; Output="([ 1, 2 ], { "hel" : "lo" })" :)
typeswitch([1,2])
case string return "string"
case $a as boolean+ | array* return $a
default return "default",
typeswitch({"hel" : "lo"})
case string | boolean? return "DS"
case $a as object? | decimal return $a
default return 4.3

(: array and object test condition :)
