(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:12:" :)
declare function fn ($i as double?, $j as string) as double+ {($i cast as string) || $j};
parallelize(fn#2(3, "hello"))

(: Invalid return type. string cannot be promoted to type double. :)

