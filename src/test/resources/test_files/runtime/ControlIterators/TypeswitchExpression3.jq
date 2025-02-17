(:JIQS: ShouldRun; Output="(0123456789ABCDEF, 4234.243 correct, AaBb, false, true)" :)
typeswitch(hexBinary("0123456789abcdef"))
case $a as base64Binary? return "not this"
case $b as boolean+ | double return ($b cast as string) || " not even this"
case $a as string | decimal+ | hexBinary* return $a
default return "wrong",
typeswitch(4.234243e3)
case $a as hexBinary+ return "not this"
case $b as boolean | double? | base64Binary? return ($b cast as string) || " correct"
case $a as string | decimal? return $a
default return "DEF",
typeswitch(base64Binary("AaBb"))
case $a as hexBinary return "not this"
case $b as boolean? | double+ return ($b cast as string) || " not even this"
case $a as string | decimal? return $a
default $def return $def,
typeswitch(false)
case $a as integer+ | double | decimal* return $a * 5
case string? return "wrong"
case $a as boolean return $a
default return duration("P3Y"),
typeswitch(duration("P4Y5MT5M"))
case $a as string | decimal+ | hexBinary? return $a
case $a as base64Binary | duration* return ends-with(($a cast as string), "5M")
default $a return $a

(: case with variables :)
