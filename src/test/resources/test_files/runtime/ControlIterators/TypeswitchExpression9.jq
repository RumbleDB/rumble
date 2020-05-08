(:JIQS: ShouldRun; Output="(default, default, default, default, default)" :)
typeswitch([1,2])
case string return "string"
case $a as boolean+ | object* return ($a, 1, 2, 3)
default return
  for $i in 1 to 5 return "default"

(: multiple items returned  :)
