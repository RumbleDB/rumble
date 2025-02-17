(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
for $i in structured-json-file("../../../queries/difficult-names.json")
where $i.keyToUse eq $i.indexToUse
return $i