(:JIQS: ShouldRun; Output="10000" :)
count(
  let $j := 10000
  return annotate(for $j in () return {"foo" : "bar"},{"foo":"string"})
)
