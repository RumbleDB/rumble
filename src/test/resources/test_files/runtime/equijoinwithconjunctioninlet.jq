(:JIQS: ShouldRun; Output="{ "foo" : "a", "bar" : "b" }" :)
let $stats := parallelize(({"foo" : "a", "bar" : "b"}))
  return
  for $scale in parallelize(({"baz" : "a", "qux" : "b"}))
  let $st := $stats[$$.foo eq $scale.baz and $$.bar eq $scale.qux]
  return $st
  
