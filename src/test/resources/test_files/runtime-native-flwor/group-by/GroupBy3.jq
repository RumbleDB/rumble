(:JIQS: ShouldRun; Output="({ "k" : 2, "c" : [ "foo", 0, "foo", 3, "foo", 6 ] }, { "k" : 1, "c" : [ "foo", -1, "foo", 2, "foo", 5, "foo", 8 ] }, { "k" : 0, "c" : [ "foo", 1, "foo", 4, "foo", 7 ] })" :)
for $i in annotate(
  for $j in 1 to 10 return {"key" : $j mod 3, "foo":{"bar":[ $j - 2 to $j ]}},
  {"key" : "integer", "foo":{"bar":["integer"]}}
)
let $l := ("foo", $i.foo.bar[[1]])
group by $k := $i.key
group by $k
order by $k descending
return { "k": $k, "c" : [ $l ] }
