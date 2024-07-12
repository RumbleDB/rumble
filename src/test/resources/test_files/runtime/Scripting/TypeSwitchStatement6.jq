(:JIQS: ShouldRun; Output="([ 1, 2 ], 1, 2, 3)" :)
variable $res;
typeswitch([1,2])
case string return $res := "string";
case $a as boolean+ | array* return $res := ($a, 1, 2, 3);
default return $res := "default";
$res

(: multiple items returned  :)
