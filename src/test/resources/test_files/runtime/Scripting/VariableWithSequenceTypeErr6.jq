(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:6:COLUMN:8:" :)
variable $a as xs:string := "string";
if ($a eq "string") then {
    while ($a eq "string") {
        variable $a as xs:int := 3;
        $a := "end";
    }
}
else $a := "too";
