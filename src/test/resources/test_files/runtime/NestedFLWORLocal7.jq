(:JIQS: ShouldRun; Output="(0, 0)" :)
for $l in ({"ints":[0]}, {"ints":[0]})
return
    for $i in $l.ints[]
    group by $int := $i
    return 0
    