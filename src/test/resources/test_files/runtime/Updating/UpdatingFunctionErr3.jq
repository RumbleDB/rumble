(:JIQS: ShouldCrash; ErrorCode="XUST0028"; ErrorMetadata="LINE:2:COLUMN:0:" :)
declare updating function local:upsert($o as object, $key as string, $val as item) as item* {
    ()
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert($je, "c", 3)
return $je
(: declared updating function has return type :)