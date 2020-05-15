(:JIQS: ShouldRun; Output="" :)
for $k in keys(keys(structured-json-file("./src/test/resources/test_data/conf-ex.json")))
group by $k
return $k
