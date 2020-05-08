(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)
let $name := "./src/test/resources/queries/conf-ex.json"
for $line in json-file($name)
return $line.guess

(: local evaluation switches to RDD evaluation :)
