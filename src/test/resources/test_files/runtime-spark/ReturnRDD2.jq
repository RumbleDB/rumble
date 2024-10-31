(:JIQS: ShouldRun; Output="10000" :)
count(
  let $i := 1
  return parallelize(1 to 10000)
)
