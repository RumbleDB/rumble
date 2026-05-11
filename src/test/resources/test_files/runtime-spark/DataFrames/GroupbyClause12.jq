(:JIQS: ShouldRun; Output="(2, 1, 1, 1)" :)
for $i in json-lines("../../../queries/conf-ex.json")
group by $y := $i.country, $t := $i.target
let $c := count($i)
order by $c descending
return $c
