(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:6:COLUMN:7:" :)
let $x := (copy $je := [1 to 4]
    modify ()
    return $je
)
return $je

(: copy variable referenced outside of transform :)
