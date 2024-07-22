(:JIQS: ShouldRun; Output="{ "a" : 1, "b" : 2, "c" : 3 }" :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    if($o.$key)
        then
            replace value of $o.$key with $val
        else
            insert $key : $val into $o
};
copy $je := {"a" : 1, "b" : 2}
modify local:upsert($je, "c", 3)
return $je
