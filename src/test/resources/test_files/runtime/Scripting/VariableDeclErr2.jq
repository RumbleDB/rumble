(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; ErrorMetadata="LINE:5:COLUMN:19:" :)
variable $res;

if (true) then {
    variable $m := $res + 2;
} else {
    variable $k := $res * 3;
}