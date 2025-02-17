(:JIQS: ShouldRun; Output="{ "1" : "0123456789ABCDEF", "2" : "4234.243 correct", "3" : "AaBb", "4" : false, "5" : true }" :)
variable $res := {};
typeswitch(hexBinary("0123456789abcdef"))
case $a as base64Binary? return insert json "1": "not this" into $res;
case $b as boolean+ | double return insert json "1": ($b cast as string) || " not even this" into $res;
case $a as string | decimal+ | hexBinary* return insert json "1": $a into $res;
default return insert json "1":"wrong" into $res;
typeswitch(4.234243e3)
case $a as hexBinary+ return insert json "2":"not this" into $res;
case $b as boolean | double? | base64Binary? return insert json "2":($b cast as string) || " correct" into $res;
case $a as string | decimal? return insert json "2":$a into $res;
default return insert json "2":"DEF" into $res;
typeswitch(base64Binary("AaBb"))
case $a as hexBinary return insert json "3":"not this" into $res;
case $b as boolean? | double+ return insert json "3":($b cast as string) || " not even this" into $res;
case $a as string | decimal? return insert json "3":$a into $res;
default $def return insert json "3":$def into $res;
typeswitch(false)
case $a as integer+ | double | decimal* return insert json "4":$a * 5 into $res;
case string? return insert json "4":"wrong" into $res;
case $a as boolean return insert json "4":$a into $res;
default return insert json "4":duration("P3Y") into $res;
typeswitch(duration("P4Y5MT5M"))
case $a as string | decimal+ | hexBinary? return insert json "5":$a into $res;
case $a as base64Binary | duration* return insert json "5":ends-with(($a cast as string), "5M") into $res;
default $a return insert json "5":$a into $res;
$res

(: case with variables :)
