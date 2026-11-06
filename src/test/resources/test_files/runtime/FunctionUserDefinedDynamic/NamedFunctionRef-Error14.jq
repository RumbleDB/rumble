(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:3:COLUMN:8:" :)
declare function fn ($i as double?, $j as decimal) as double+ {$i + $j};
fn#2(3, ())

(: Invalid argument. Expecting one item, but the value provided is the empty sequence. :)
