(:JIQS: ShouldRun; Output="AaBb" :)
typeswitch(base64Binary("AaBb"))
case $a as hexBinary+ return "not this"
case $b as boolean | double+ return ($b cast as string) || " not even this"
case $a as string? | decimal return $a
default $def return $def

(: case with variables, return default case :)
