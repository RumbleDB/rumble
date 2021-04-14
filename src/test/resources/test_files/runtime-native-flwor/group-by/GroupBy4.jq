(:JIQS: ShouldRun; Output="({ "k" : 1, "c" : [ [ [ -1, 2, 5, 8 ] ] ] }, { "k" : 2, "c" : [ [ [ 0, 3, 6 ] ] ] }, { "k" : 0, "c" : [ [ [ 1, 4, 7 ] ] ] })" :)
for $i in annotate(
  for $j in 1 to 10 return {"key" : $j mod 3, "foo":{"bar":[ $j - 2 to $j ]}},
  {"key" : "integer", "foo":{"bar":["integer"]}}
)
let $l := $i.foo.bar[[1]]
group by $k := $i.key
return { "k": $k, "c" : [ $l ] }
