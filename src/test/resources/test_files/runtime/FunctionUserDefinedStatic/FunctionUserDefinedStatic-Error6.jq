(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare function price($x as decimal, $y as double) as double? { $x + $y };
price(1, ())

(: Empty sequence is not expected here. The expected type is xs:double :)
