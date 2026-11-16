(:JIQS: ShouldCrash; ErrorCode="XUST0001" :)
(: local:upsert does not use $o, so inlining it would drop the updating argument before it is reported. :)
declare updating function local:upsert($o as object, $key as string, $val as item) {
    ()
};
let $je := {"a" : 1, "b" : 2}
return local:upsert(insert json "c" : 3 into $je, "c", 3)
