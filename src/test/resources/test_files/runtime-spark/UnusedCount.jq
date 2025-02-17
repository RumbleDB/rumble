(:JIQS: ShouldRun; Output="10000" :)
count(
  for $o in parallelize(1 to 10000)
  count $p
  return $o
)