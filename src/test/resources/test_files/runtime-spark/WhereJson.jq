(:JIQS: ShouldRun; Output="(Latvian, Russian)" :)
for $i in json-file("./src/test/resources/test_data/conf-ex.json")
where $i."target" eq "Russian"
return $i."guess"