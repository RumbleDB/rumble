(:JIQS: ShouldRun; Output=(1, 49) :)
for $x in ("1", 49)
group by $x
order by string($x)
return $x
