(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare function price ($x as decimal?, $y as double) as double* { (($x + $y) cast as string) || "Hello!" };
price(1, 2)

(: Invalid return type. string cannot be promoted to type double*. :)
