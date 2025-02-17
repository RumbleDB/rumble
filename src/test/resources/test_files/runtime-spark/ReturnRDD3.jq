(:JIQS: ShouldRun; Output="0" :)
count(
  for $i in ()
  return parallelize(1 to 10000)
)

