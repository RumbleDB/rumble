(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:0:" :)
variable $je := {"a" : 1};
replace value of json $je."b" with "foo";
$je

(: selector string does not exist in target object :)