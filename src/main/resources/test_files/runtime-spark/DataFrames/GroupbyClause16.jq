(:JIQS: ShouldRun; Output="(1, 2, 1, 1, 1)" :)
for $i in json-file("./src/main/resources/queries/conf-ex.json")
group by $y := $i.country, $t := $i.target
for $n in 1 to count($i)
return $n