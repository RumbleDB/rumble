(:JIQS: ShouldRun; Output="(choices, country, date, guess, sample, target)" :)
for $k in keys(structured-json-file("../../../queries/conf-ex.json"))
order by $k
return $k
