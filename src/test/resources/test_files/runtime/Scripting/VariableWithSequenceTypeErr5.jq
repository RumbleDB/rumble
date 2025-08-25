(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:5:COLUMN:8:" :)
variable $a as xs:string := "string";
if ($a eq "string") then {
    while ($a eq "string") {
        $a := 3.45;
    }
}
else $a := "too";
