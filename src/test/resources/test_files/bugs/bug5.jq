(:JIQS: ShouldCrash; ErrorCode="RBDY0005"; ErrorMetadata="LINE:5:COLUMN:13:" :)
count(
let $doc := json-lines("../../queries/confusion_sample.json")
for $d in $doc
return count($doc)
)
