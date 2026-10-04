(:JIQS: ShouldRun; Output="(false, false)" :)
for $x in (xs:hexBinary("00"), xs:base64Binary("AA=="))
return ($x lt $x) is statically xs:boolean
