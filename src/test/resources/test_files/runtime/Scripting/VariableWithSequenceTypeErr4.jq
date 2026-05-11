(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:25:" :)
variable $a as xs:string := "string";
if ($a eq "string") then $a := 3;
else $a := "too";
