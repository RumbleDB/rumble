(:JIQS: ShouldRun; Output="(0-1, 0-2, 1-1, 1-2)" :)
(: The recursive call re-enters the same return clause while the outer loop is still running. :)
declare updating function local:f($o as object, $n as integer) {
    for $i in (1, 2)
    return (
        if ($n gt 0 and $i eq 1) then local:f($o, $n - 1) else (),
        insert json { $n || "-" || $i : $n } into $o
    )
};
copy $o := {}
modify local:f($o, 1)
return for $k in keys($o) order by $k return $k
