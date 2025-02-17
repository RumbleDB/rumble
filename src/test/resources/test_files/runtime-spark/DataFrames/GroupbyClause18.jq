(:JIQS: ShouldRun; Output="1" :)
for $game in parallelize(1) group by $c := 0 return count($game[1]) div count($game)
