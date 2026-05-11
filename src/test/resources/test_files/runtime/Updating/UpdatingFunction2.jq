(:JIQS: ShouldRun; Output="{ "a" : 1, "b" : 2 }" :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    ()
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert($je, "c", 3)
return $je
