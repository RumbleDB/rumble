(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:3:COLUMN:1:" :)
variable $je := [1 to 4];
(3, delete json $je[[1]], delete json $je[[2]]);
$je

(: at least one expr is not simple :)