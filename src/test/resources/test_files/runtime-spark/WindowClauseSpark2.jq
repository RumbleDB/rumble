(:JIQS: ShouldRun; Output="({ "from" : 2, "days" : 3 }, { "from" : 7, "days" : 2 })" :)
for tumbling window $w in parallelize((24, 31, 33, 32, 27, 29, 34, 35, 26))
start $first at $s when $first ge 30
end next $n when $n lt 30
return { "from" : $s, "days" : count($w) }

(: windows over a distributed input are computed locally :)
