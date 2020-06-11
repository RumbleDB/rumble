(:JIQS: ShouldRun; Output="" :)
for $k in keys(keys(structured-json-file("../../../queries/conf-ex.json")))
group by $k
return $k
