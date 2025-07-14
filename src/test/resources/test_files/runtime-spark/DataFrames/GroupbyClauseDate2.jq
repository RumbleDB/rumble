(:JIQS: ShouldRun; Output="(1900-01-01, 2004-04-12)" :)
for $j as date in parallelize((date("1900-01-01"), date("2004-04-12")))
group by $j
order by $j
return $j
