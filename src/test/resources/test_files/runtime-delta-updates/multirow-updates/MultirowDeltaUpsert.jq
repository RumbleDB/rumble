(:JIQS: ShouldRun; UpdateDim=[2,7]; Output="" :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    if($o.$key)
        then
            replace value of json $o.$key with $val
        else
            insert json $key : $val into $o
};
for $data in delta-file("./tempDeltaTable")
count $c
return (
    local:upsert($data, "is_1", $c eq 1),
    local:upsert($data, "is_not_1", $c ne 1)
)
