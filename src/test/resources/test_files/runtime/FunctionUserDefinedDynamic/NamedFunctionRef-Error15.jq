(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:0:" :)
declare function fn ($i as double?, $j as decimal) as double+ {$i + $j};
fn#2(3, "hello")

(: Invalid argument. string cannot be promoted to type decimal. :)
