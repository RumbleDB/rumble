(:JIQS: ShouldRun; Output="10" :)
for $i in annotate(
  for $j in 1 to 10 return {"foo":{"bar":[ 1 to $j ]}},
  {"foo":{"bar":["integer"]}}
)
let $g := $i.foo
let $h := $g.bar
let $j := $h[[1]]
group by $j
return count($j)