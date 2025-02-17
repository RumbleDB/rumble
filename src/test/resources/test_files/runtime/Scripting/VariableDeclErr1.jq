(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; ErrorMetadata="LINE:5:COLUMN:12:" :)
variable $res;

while (true) {
    $res := $res + 2;
    if ($res eq 2) then break loop;
    else ();
}