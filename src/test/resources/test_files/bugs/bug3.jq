(:JIQS: ShouldRun; Output="500" :)
count(
let $doc := json-file("../../queries/confusion_sample.json")
for $d in $doc
return 1
)
