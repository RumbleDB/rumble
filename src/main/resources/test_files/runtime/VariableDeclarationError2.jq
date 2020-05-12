(:JIQS: ShouldNotCompile; ErrorCode="XPST0008"; ErrorMetadata="LINE:4:COLUMN:35:" :)
declare variable $x as integer := 1;
declare variable $y as integer? := $x + 1;
declare variable $z as integer+ := $w + $y + 2;

$z

