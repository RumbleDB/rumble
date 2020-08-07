(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:4:COLUMN:14:" :)
for $var as integer in 1 to 10
let $j := let $internal := 2 return $internal
where $var eq $internal
return $var