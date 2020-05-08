(:JIQS: ShouldRun; Output="([ 1, 2 ], 1, 2, 3)" :)
typeswitch([1,2])
case string return "string"
case $a as boolean+ | array* return ($a, 1, 2, 3)
default return "default"

(: multiple items returned  :)
