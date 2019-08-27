(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
for $i in json-file("./src/main/resources/queries/conf-ex.json")
return $i."target"