(:JIQS: ShouldRun; Output="1500" :)
count(for $i in 1 to 5000 where $i le 1500 return $i)

(: local execution - avoids truncation of spark execution :)
