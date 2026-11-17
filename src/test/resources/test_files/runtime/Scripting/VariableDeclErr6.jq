(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; ErrorMetadata="LINE:5:COLUMN:27:" :)
variable $res := 1, $counter := $res + 1;
while ($counter lt 4) {
    variable $res;
    $counter := $counter + $res;
}
$counter