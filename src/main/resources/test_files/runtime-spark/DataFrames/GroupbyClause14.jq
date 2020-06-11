(:JIQS: ShouldRun; Output="(2, 1, 1, 1)" :)
for $i in json-file("../../../queries/conf-ex.json")
group by $y := $i.country, $t := $i.target
order by count($i) descending
return count($i)
