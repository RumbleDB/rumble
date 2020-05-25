(:JIQS: ShouldRun; Output="({ "$guess" : "Bulgarian", "$i" : [ { "guess" : "Bulgarian", "choices" : [ "Albanian", "Bulgarian", "Russian", "Ukrainian" ] }, { "guess" : "Bulgarian", "choices" : [ "Arabic", "Bulgarian", "Hebrew", "Spanish" ] } ] }, { "$guess" : "Arabic", "$i" : [ { "guess" : "Arabic", "choices" : [ "Arabic", "Cantonese", "Maltese", "Samoan" ] }, { "guess" : "Arabic", "choices" : [ "Albanian", "Arabic", "Czech", "Dutch" ] } ] })" :)
for $i in parallelize((
 {"guess": "Arabic", "choices": ["Arabic", "Cantonese", "Maltese", "Samoan"]},
 {"guess": "Arabic", "choices": ["Albanian", "Arabic", "Czech", "Dutch"]},
 {"guess": "Bulgarian", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"]},
 {"guess": "Bulgarian", "choices": ["Arabic", "Bulgarian", "Hebrew", "Spanish"]}))
let $guess := $i.guess
group by $guess
return {"$guess": $guess, "$i": $i}

(: test with no null or empty entries - grouping variable pre-defined with let :)
