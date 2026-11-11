(:JIQS: ShouldRun; Output="(29.3, 32, 30.7, 29.3, 30, 32.7, 31.7)" :)
for sliding window $w in parallelize((24, 31, 33, 32, 27, 29, 34, 35, 26))
start at $s when true
only end at $e when $e - $s eq 2
return round(avg($w), 1)

(: windows over a distributed input are computed locally :)
