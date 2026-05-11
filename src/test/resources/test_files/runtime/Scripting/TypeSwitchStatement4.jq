(:JIQS: ShouldRun; Output="AaBb" :)
variable $res;
typeswitch(base64Binary("AaBb"))
case $a as hexBinary+ return $res := "not this";
case $b as boolean | double+ return $res := ($b cast as string) || " not even this";
case $a as string? | decimal return $res := $a;
default $def return $res := $def;
$res
(: case with variables, return default case :)
