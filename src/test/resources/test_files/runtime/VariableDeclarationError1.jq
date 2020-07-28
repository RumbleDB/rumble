(:JIQS: ShouldCrash; ErrorCode="XQST0049"; ErrorMetadata="LINE:4:COLUMN:0:" :)
declare variable $x as integer := 1;
declare variable $y as integer? := $x + 1;
declare variable $x as integer+ := $x + $y + 2;

$z

