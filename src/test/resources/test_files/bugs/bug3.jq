(:JIQS: ShouldRun; Output="3" :)
count(
let $doc := json-file("../../queries/confusion_sample.json")
for $d in $doc
group by $c := $d.country
return 1
)
