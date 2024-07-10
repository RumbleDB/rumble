(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:2:COLUMN:34:" :)
copy json $je := [1 to 4], $ej := $je
modify ()
return $ej

(: copy declaration references value in same copy clause :)
