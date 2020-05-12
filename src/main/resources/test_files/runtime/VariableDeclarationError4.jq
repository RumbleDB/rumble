(:JIQS: ShouldCrash; ErrorCode="XPDY0002"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare variable $x as integer := 1;
declare variable $y as integer? external;
declare variable $z as integer? := $x + $y + 2;

$z

