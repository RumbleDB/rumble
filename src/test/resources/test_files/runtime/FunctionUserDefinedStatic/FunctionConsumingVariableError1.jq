(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:4:COLUMN:4:" :)
declare variable $x as integer := 1; 
declare function udf1 ($i as integer) as boolean {
    $y < $i
};
udf1(3), udf1(0)
