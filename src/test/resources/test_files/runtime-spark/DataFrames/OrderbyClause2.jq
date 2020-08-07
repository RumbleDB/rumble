(:JIQS: ShouldRun; Output="({ "guess" : "Arabic", "target" : "null", "country" : "IN", "choices" : [ "Arabic", "Greek", "Hebrew", "Spanish" ], "sample" : "8903e496bf04c3a85a70e1dc5e0da5f1", "date" : "2013-08-20" }, { "guess" : "Arabic", "target" : "null", "country" : "AU", "choices" : [ "Albanian", "Arabic", "Czech", "Dutch" ], "sample" : "cd438a022d83ce4ded75d4f9c6ea7239", "date" : "2013-08-20" }, { "guess" : "Tagalog", "target" : "null", "country" : "AU", "choices" : [ "Arabic", "Estonian", "Tagalog", "Malayalam" ], "sample" : "154ac3670f21586ab168cd376290bcb7", "date" : "2013-08-20" })" :)
for $i in parallelize((
 {"guess": "Arabic", "target": "null", "country": "AU", "choices": ["Albanian", "Arabic", "Czech", "Dutch"], "sample": "cd438a022d83ce4ded75d4f9c6ea7239", "date": "2013-08-20"},
 {"guess": "Arabic", "target": "null", "country": "IN", "choices": ["Arabic", "Greek", "Hebrew", "Spanish"], "sample": "8903e496bf04c3a85a70e1dc5e0da5f1", "date": "2013-08-20"},
 {"guess": "Tagalog", "target": "null", "country": "AU", "choices": ["Arabic", "Estonian", "Tagalog", "Malayalam"], "sample": "154ac3670f21586ab168cd376290bcb7", "date": "2013-08-20"}))
let $guess := $i."guess", $target := $i."target"
order by $target, $i.country descending, $i.date descending
return $i

(: null target, descending country :)
