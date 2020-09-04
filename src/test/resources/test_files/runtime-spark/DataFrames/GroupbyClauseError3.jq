(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
for $i in parallelize((
 {"guess": "Bulgarian", "target": "Albanian", "country": "AU", "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"},
 {"guess": "Bulgarian", "target": "Albanian", "country": ["AU"], "choices": ["Albanian", "Bulgarian", "Russian", "Ukrainian"], "sample": "00b85faa8b878a14f8781be334deb137", "date": "2013-08-19"}))
group by $j := $i.country
return $i


(: non-atomic group by error - array :)
