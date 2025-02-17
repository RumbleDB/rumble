(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:6:COLUMN:20:" :)
typeswitch(base64Binary("AaBb"))
case $a as hexBinary+ return "not this"
case $b as boolean | double+ return ($b cast as string) || " not even this"
case $a as string? | decimal return $a
default $def return $asd

(: Uninitialized variable reference in default expression :)
