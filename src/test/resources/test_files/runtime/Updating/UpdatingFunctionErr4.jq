(:JIQS: ShouldCrash; ErrorCode="XUST0002"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    $val
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert($je, "c", 3)
return $je
(: updating function has simple body :)