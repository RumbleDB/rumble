(:JIQS: ShouldRun; Output="1" :)
for $i in parallelize(1) for $l in (let $i := parallelize(1) return 1)
return $i

(: Let clause is locally evaluated :)
