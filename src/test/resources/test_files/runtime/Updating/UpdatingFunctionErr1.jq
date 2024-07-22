(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:6:COLUMN:20:" :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    ()
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert(insert "c" : 3 into $je, "c", 3)
return $je
(: argument of function call is not simple :)