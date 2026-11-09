(:JIQS: ShouldRun; Output="5.5" :)
for $i in parallelize(1 to 10) group by $x := true return avg($i)