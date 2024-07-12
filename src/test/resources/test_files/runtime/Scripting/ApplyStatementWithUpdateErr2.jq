(:JIQS: ShouldCrash; ErrorCode="JNUP0009"; ErrorMetadata="LINE:3:COLUMN:1:" :)
variable $je := [1 to 4];
(replace value of json $je[[1]] with "foo", replace value of json $je[[1]] with "bar");
$je

(: replace at same array with same selector :)