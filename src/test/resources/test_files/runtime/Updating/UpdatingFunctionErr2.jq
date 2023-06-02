(:JIQS: ShouldCrash; ErrorCode="XUST0001"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare function local:upsert($o as object, $key as string, $val as item) {
    insert $key : $val into $o
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert($je, "c", 3)
return $je
(: non-updating function has non-simple body :)