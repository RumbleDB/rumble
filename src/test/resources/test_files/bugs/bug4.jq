(:JIQS: ShouldRun; Output="500" :)
let $doc := json-file("../../queries/confusion_sample.json")
for $d in $doc
group by $c := $d.country
return count($d)

