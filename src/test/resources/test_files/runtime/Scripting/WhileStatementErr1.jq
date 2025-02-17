(:JIQS: ShouldCrash; ErrorCode="XPDY0130"; ErrorMetadata="LINE:3:COLUMN:8:" :)
variable $counter;
while ( $counter lt 10 ) {
    $counter := $counter + 1;
}
$counter

(: uninitialized variable is null :)