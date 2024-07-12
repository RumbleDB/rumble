(:JIQS: ShouldRun; Output="(default, default, default, default, default)" :)
variable $res;
typeswitch([1,2])
case string return $res := "string";
case $a as boolean+ | object* return $res := ($a, 1, 2, 3);
default return
  $res := for $i in 1 to 5 return "default";
$res

(: multiple items returned  :)
