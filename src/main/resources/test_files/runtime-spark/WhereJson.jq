(:JIQS: ShouldRun; Output="(Latvian, Russian)" :)
for $i in json-file("./src/test/resources/queries/conf-ex.json")
where $i."target" eq "Russian"
return $i."guess"
