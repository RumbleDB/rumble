(:JIQS: ShouldCrash; ErrorCode="RBDY0006"; ErrorMetadata="LINE:1:COLUMN:0:" :)
for $j in parallelize((date("2004-04-12"), date("2004-04-12-05:00"), date("2004-04-12Z"), date("2004-04-12+14:00"), date("-0045-01-01"), date("12004-04-12Z"), date(())))
group by $j
order by $j
return $j
