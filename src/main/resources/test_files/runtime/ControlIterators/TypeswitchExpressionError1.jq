(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:5:COLUMN:36:" :)
typeswitch(4.234243e3)
case $a as hexBinary return "not this"
case $b as boolean? | double | base64Binary return $b
case $a as string | decimal+ return $r
default return "DEF"

(: Uninitialized variable reference :)
