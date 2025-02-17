(:JIQS: ShouldRun; Output="([ 1, { "guess" : "Bulgarian", "choices" : [ "Albanian", "Bulgarian", "Russian", "Ukrainian" ] }, { "guess" : "Bulgarian", "choices" : [ "Arabic", "Bulgarian", "Hebrew", "Spanish" ] } ], [ 2, { "guess" : "Arabic", "choices" : [ "Arabic", "Cantonese", "Maltese", "Samoan" ] }, { "guess" : "Arabic", "choices" : [ "Albanian", "Arabic", "Czech", "Dutch" ] } ])" :)
for $i in parallelize((
 {"guess": "Arabic", "choices": ["Arabic", "Cantonese", "Maltese", "Samoan"]},
 {"guess": "Arabic", "choices": ["Albanian", "Arabic", "Czech", "Dutch"]},
 {"guess": "Bulgarian", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"]},
 {"guess": "Bulgarian", "choices": ["Arabic", "Bulgarian", "Hebrew", "Spanish"]}))
group by $guess := $i.guess
count $group_index
return [$group_index, $i]

(: test with no null or empty entires - grouping variable defined in place:)
