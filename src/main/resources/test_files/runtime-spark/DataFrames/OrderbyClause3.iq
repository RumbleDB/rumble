(:JIQS: ShouldRun; Output="({ "guess" : "Arabic", "target" : "Arabic", "country" : "IN", "choices" : [ "Arabic", "Greek", "Hebrew", "Spanish" ], "sample" : "8903e496bf04c3a85a70e1dc5e0da5f1", "date" : "2013-08-20" }, { "guess" : "Tagalog", "target" : "Arabic", "country" : "AU", "choices" : [ "Arabic", "Estonian", "Tagalog", "Malayalam" ], "sample" : "154ac3670f21586ab168cd376290bcb7", "date" : "2013-08-20" }, { "guess" : "Arabic", "target" : "Arabic", "country" : null, "choices" : [ "Arabic", "Cantonese", "Maltese", "Samoan" ], "sample" : "83c6ffcb085ce9c18c0e2aff00dda865", "date" : "2013-08-20" }, { "guess" : "Arabic", "target" : "Arabic", "country" : null, "choices" : [ "Arabic", "Bulgarian", "Latvian", "Urdu" ], "sample" : "8903e496bf04c3a85a70e1dc5e0da5f1", "date" : "2013-08-19" })" :)
for $i in parallelize((
 {"guess": "Arabic", "target": "Arabic", "country": null, "choices": ["Arabic", "Bulgarian", "Latvian", "Urdu"], "sample": "8903e496bf04c3a85a70e1dc5e0da5f1", "date": "2013-08-19"},
 {"guess": "Arabic", "target": "Arabic", "country": null, "choices": ["Arabic", "Cantonese", "Maltese", "Samoan"], "sample": "83c6ffcb085ce9c18c0e2aff00dda865", "date": "2013-08-20"},
 {"guess": "Arabic", "target": "Arabic", "country": "IN", "choices": ["Arabic", "Greek", "Hebrew", "Spanish"], "sample": "8903e496bf04c3a85a70e1dc5e0da5f1", "date": "2013-08-20"},
 {"guess": "Tagalog", "target": "Arabic", "country": "AU", "choices": ["Arabic", "Estonian", "Tagalog", "Malayalam"], "sample": "154ac3670f21586ab168cd376290bcb7", "date": "2013-08-20"}))
let $guess := $i."guess", $target := $i."target"
order by $target, $i.country descending, $i.date descending
return $i

(: equal target, null country, descending date :)
