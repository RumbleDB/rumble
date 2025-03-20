(:JIQS: ShouldRun; Output="(Russian, Russian, Czech, Serbian, Serbian)" :)
for $i in json-lines("../../queries/conf-ex.json")
return $i."target"
