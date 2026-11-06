(:JIQS: ShouldRun; Output="10000" :)
count(
  for $i in 1
  return parallelize(1 to 10000)
)
