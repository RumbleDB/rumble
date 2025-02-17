(:JIQS: ShouldRun; Output="0" :)
count(
  let $j := 10000
  return annotate(for $i in () return {"foo" : "bar"},{"foo":"string"})
)
