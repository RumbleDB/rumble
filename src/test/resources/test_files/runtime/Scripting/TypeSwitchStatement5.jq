(:JIQS: ShouldRun; Output="{ "1" : [ 1, 2 ], "2" : { "hel" : "lo" } }" :)
variable $res := {};
typeswitch([1,2])
case string return insert json "1" : "string" into $res;
case $a as boolean+ | array* return insert json "1" : $a into $res;
default return insert json "1" : "default" into $res;
typeswitch({"hel" : "lo"})
case string | boolean? return insert json "2" :"DS" into $res;
case $a as object? | decimal return insert json "2" :$a into $res;
default return insert json "2" :4.3 into $res;
$res

(: array and object test condition :)
