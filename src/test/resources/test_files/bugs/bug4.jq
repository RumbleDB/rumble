(:JIQS: ShouldRun; Output="(46, 936, 18)" :)
let $doc := json-lines("../../queries/confusion_sample.json")
let $x := 1
for $d in $doc
group by $c := $d.country
return count($d) + count($x)

