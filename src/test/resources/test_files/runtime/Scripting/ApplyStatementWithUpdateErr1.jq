(:JIQS: ShouldCrash; ErrorCode="JNUP0016"; ErrorMetadata="LINE:3:COLUMN:0:" :)
variable $je := {"a" : 1};
replace json value of $je."b" with "foo";
$je

(: selector string does not exist in target object :)