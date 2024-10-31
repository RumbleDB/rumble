(:JIQS: ShouldRun; Output="(false, false)" :)
for $i in parallelize(1 to 2)
let $j := true
order by $j
count $c
group by $c
return false

(: empty tuple projection after a group by :)