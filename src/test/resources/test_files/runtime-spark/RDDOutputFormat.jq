(:JIQS: ShouldRun; Output="(Latvian, Russian, Czech, Greek, Serbian)" :)

for $o in json-file("./src/test/resources/test_data/conf-ex.json") return $o.guess

(: ensure correct output format using parantheses and commas for RDDs :)
