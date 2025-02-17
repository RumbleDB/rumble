(:JIQS: ShouldCrash; ErrorCode="RBST0003"; ErrorMetadata="LINE:5:COLUMN:7:" :)
count(
let $doc := json-file("../../queries/confusion_sample.json")
for $d in $doc
return count($doc)
)
