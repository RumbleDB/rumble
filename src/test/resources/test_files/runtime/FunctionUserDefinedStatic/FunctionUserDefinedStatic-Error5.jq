(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:9:" :)
declare function price($x as decimal?, $y as double) as double* { $x + $y };
price(1, "hello")

(: Invalid argument. string cannot be promoted to type double. :)
