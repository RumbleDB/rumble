(:JIQS: ShouldRun; Output="(1, 2)" :)
for $i in json-file("./src/test/resources/test_data/conf-ex.json")
group by $y := $i.country, $t := $i.target
group by $j := count($i)
order by $j
return $j