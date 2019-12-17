(:JIQS: ShouldRun; Output="(1, 2)" :)
for $i in json-file("./src/main/resources/queries/conf-ex.json")
group by $y := $i.country, $t := $i.target
group by $j := count($i)
return $j