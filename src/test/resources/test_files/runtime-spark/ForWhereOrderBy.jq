(:JIQS: ShouldRun; Output="({ "guess" : "Czech", "target" : "Czech", "country" : "SE", "choices" : [ "Maori", "Czech", "Korean", "Turkish" ], "sample" : "1787b5c79a00b3513ce76847bc1f5b75", "date" : "2013-08-20" }, { "guess" : "Russian", "target" : "Russian", "country" : "AU", "choices" : [ "Croatian", "Nepali", "Russian", "Slovenian" ], "sample" : "8a59d48e99e8a1df7e366c4648095e27", "date" : "2013-08-20" }, { "guess" : "Serbian", "target" : "Serbian", "country" : "AU", "choices" : [ "Dari", "Serbian", "Sinhalese", "Vietnamese" ], "sample" : "0d5b697ebb326b5043ce7fa60a7b968d", "date" : "2013-08-20" })" :)
for $i in json-lines("../../queries/conf-ex.json")
let $guess := $i."guess", $target := $i."target"
where $guess eq $target
order by $target, $i.country descending, $i.date descending
return $i
