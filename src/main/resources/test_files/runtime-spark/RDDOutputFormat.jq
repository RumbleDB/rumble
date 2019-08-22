(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)

for $o in json-file("./src/main/resources/queries/conf-ex.json") return $o.guess

(: ensure correct output format using parantheses and commas for RDDs :)
