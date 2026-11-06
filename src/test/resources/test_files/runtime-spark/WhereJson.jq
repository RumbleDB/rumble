(:JIQS: ShouldRun; Output="(Latvian, Russian)" :)
for $i in json-lines("../../queries/conf-ex.json")
where $i."target" eq "Russian"
return $i."guess"
