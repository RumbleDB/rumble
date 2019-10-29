(:JIQS: ShouldRun; Output="({ "guess" : "Bulgarian", "target" : 123, "country" : "AU", "choices" : [ "Albanian", "Bulgarian", "Russian", "Ukrainian" ], "sample" : "00b85faa8b878a14f8781be334deb137", "date" : "2013-08-19" }, { "guess" : "Bulgarian", "target" : 123, "country" : "AU", "choices" : [ "Albanian", "Bulgarian", "Russian", "Ukrainian" ], "sample" : "00b85faa8b878a14f8781be334deb137", "date" : "2013-08-19" }, { "guess" : "Bulgarian", "target" : 123000, "country" : "AU", "choices" : [ "Albanian", "Bulgarian", "Russian", "Ukrainian" ], "sample" : "00b85faa8b878a14f8781be334deb137", "date" : "2013-08-19" })" :)
for $i in parallelize((
 {"guess": "Bulgarian", "target": 123, "country": "AU", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"},
 {"guess": "Bulgarian", "target": 123.0, "country": "AU", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"},
 {"guess": "Bulgarian", "target": 123e+3, "country": "AU", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"}))
let $guess := $i."guess", $target := $i."target"
order by $target empty greatest, $i.country descending empty least, $i.date descending empty greatest
return $i

(: different numeric types used together :)
