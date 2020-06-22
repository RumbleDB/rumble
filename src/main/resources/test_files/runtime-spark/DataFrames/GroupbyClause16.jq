(:JIQS: ShouldRun; Output="(1, 1, 1, 1, 2)" :)
for $i in json-file("../../../queries/conf-ex.json")
group by $y := $i.country, $t := $i.target
order by count($i)
for $n in 1 to count($i)
return $n
