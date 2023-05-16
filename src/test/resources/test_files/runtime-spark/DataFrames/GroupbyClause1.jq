(:JIQS: ShouldRun; Output="([ 1, { "guess" : "Czech", "target" : "Czech", "country" : "SE", "choices" : [ "Maori", "Czech", "Korean", "Turkish" ], "sample" : "1787b5c79a00b3513ce76847bc1f5b75", "date" : "2013-08-20" } ], [ 2, { "guess" : "Latvian", "target" : "Russian", "country" : "AU", "choices" : [ "Lao", "Latvian", "Russian", "Swahili" ], "sample" : "b7df3f9d67cef259fbcaa5abcad9d774", "date" : "2013-08-20" }, { "guess" : "Russian", "target" : "Russian", "country" : "AU", "choices" : [ "Croatian", "Nepali", "Russian", "Slovenian" ], "sample" : "8a59d48e99e8a1df7e366c4648095e27", "date" : "2013-08-20" } ], [ 3, { "guess" : "Greek", "target" : "Serbian", "country" : "SE", "choices" : [ "German", "Greek", "Kannada", "Serbian" ], "sample" : "0d5b697ebb326b5043ce7fa60a7b968d", "date" : "2013-08-20" }, { "guess" : "Serbian", "target" : "Serbian", "country" : "AU", "choices" : [ "Dari", "Serbian", "Sinhalese", "Vietnamese" ], "sample" : "0d5b697ebb326b5043ce7fa60a7b968d", "date" : "2013-08-20" } ])" :)
for $i in json-file("../../../queries/conf-ex.json", 10)
let $j := $i.target
group by $j
order by $j
count $k
return [$k, $i]

(: test with no null or empty entires :)
