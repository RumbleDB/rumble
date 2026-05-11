(:JIQS: ShouldRun; Output="" :)
for $k in keys(keys(structured-json-lines("../../../queries/conf-ex.json")))
group by $k
return $k
