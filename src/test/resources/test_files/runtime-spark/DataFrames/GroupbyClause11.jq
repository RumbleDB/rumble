(:JIQS: ShouldRun; Output="0" :)
for $i in parallelize((0, 0.0))
group by $i
return $i
