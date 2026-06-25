(:JIQS: ShouldRun; Output="({"Land" : "a", "Schule" : "b"})" :)
let $stats := parallelize(({"Land" : "a", "Schule" : "b"}))
  return
  for $scale in parallelize(({"Country" : "a", "University" : "b"}))
  let $st := $stats[$$.Land eq $scale.Country and $$.Schule eq $scale.University]
  return $st
  
