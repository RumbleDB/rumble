(:JIQS: ShouldRun; Output="100" :)
exit returning count(
  let $j := 100
  return annotate(for $i in 1 to $j return {"foo" : "bar"},{"foo":"string"})
);