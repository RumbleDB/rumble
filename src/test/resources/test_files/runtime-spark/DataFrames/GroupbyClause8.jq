(:JIQS: ShouldRun; Output="(Czech, Russian, Serbian)" :)
for $i in json-lines("../../../queries/conf-ex.json", 10)
group by $target := $i.target
order by $target
return $target

(: variables defined in groupby are accessible all the way to and including the return clause:)
