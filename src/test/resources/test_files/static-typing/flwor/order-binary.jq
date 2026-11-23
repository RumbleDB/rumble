(:JIQS: ShouldRun; Output="(0AD7, 0CD7)" :)
(: XQuery 3.1 orders binary values. :)
for $a in ("0cd7" cast as hexBinary, "0ad7" cast as hexBinary)
order by $a
return string($a)
