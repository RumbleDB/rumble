(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
for $i in json-file("../../queries/conf-ex.json")
return $i."target"
