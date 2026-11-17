(:JIQS: ShouldParse :)
typeswitch(hexBinary("0123456789abcdef"))
case $a as base64Binary? return {"not this";}
case $b as boolean+ | double return {($b cast as string) || " not even this";}
case $a as string | decimal+ | hexBinary* return {$a;}
default return {"wrong";}