(:JIQS: ShouldCrash; ErrorCode="XPTY0004"; ErrorMetadata="LINE:6:COLUMN:0:" :)
for $i in parallelize((
 {"guess": "Bulgarian", "target": "Albanian", "country": "AU", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"},
 {"guess": "Bulgarian", "target": "Albanian", "country": 123, "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"}))
let $guess := $i."guess", $target := $i."target"
order by $target empty greatest, $i.country descending empty least, $i.date descending empty greatest
return $i

(: orderby non-matching types :)
