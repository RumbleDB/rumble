(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:4:COLUMN:16:" :)
variable $counter;
while ( $counter lt 10 ) {
    $counter := $counter + 1;
}
$counter

(: uninitialized variable is null, therefore we should get invalid operation error :)