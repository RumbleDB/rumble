(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
for $i in json-file("./src/test/resources/test_data/conf-ex.json")
return $i."target"