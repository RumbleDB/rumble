(:JIQS: ShouldRun; Output="" :)
for $k in keys(keys(structured-json-file("./src/test/resources/queries/conf-ex.json")))
group by $k
return $k
