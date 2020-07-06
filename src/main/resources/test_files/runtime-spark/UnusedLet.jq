(:JIQS: ShouldRun; Output="10000" :)
count(
  for $o in parallelize(1 to 10000)
  let $p := 1 to 100000000
  return $o
)