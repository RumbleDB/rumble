(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)
let $name := "../../../queries/conf-ex.json"
for $line in json-lines($name)
return $line.guess

(: local evaluation switches to RDD evaluation :)
