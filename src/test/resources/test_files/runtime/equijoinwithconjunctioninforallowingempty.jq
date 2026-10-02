(:JIQS: ShouldRun; Output="([ { "foo" : "a", "bar" : "b" } ], [ ])" :)
let $stats := parallelize(({"foo" : "a", "bar" : "b"}))
  return
  for $scale in parallelize(({"baz" : "a", "qux" : "b"}, {"baz" : "x", "qux" : "y"}))
  for $st allowing empty in $stats[$$.foo eq $scale.baz and $$.bar eq $scale.qux]
  return [ $st ]
