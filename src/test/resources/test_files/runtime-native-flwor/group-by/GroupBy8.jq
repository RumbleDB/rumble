(:JIQS: ShouldRun; Output="({ "k" : 0, "c" : [ { "key" : 0, "foo" : { "bar" : [ 1, 2, 3 ] } }, { "key" : 0, "foo" : { "bar" : [ 4, 5, 6 ] } }, { "key" : 0, "foo" : { "bar" : [ 7, 8, 9 ] } } ], "d" : [ 0, 0, 0 ], "e" : [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ], "f" : [ [ 1, 2, 3 ], [ 4, 5, 6 ], [ 7, 8, 9 ] ] }, { "k" : 1, "c" : [ { "key" : 1, "foo" : { "bar" : [ -1, 0, 1 ] } }, { "key" : 1, "foo" : { "bar" : [ 2, 3, 4 ] } }, { "key" : 1, "foo" : { "bar" : [ 5, 6, 7 ] } }, { "key" : 1, "foo" : { "bar" : [ 8, 9, 10 ] } } ], "d" : [ 1, 1, 1, 1 ], "e" : [ -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ], "f" : [ [ -1, 0, 1 ], [ 2, 3, 4 ], [ 5, 6, 7 ], [ 8, 9, 10 ] ] }, { "k" : 2, "c" : [ { "key" : 2, "foo" : { "bar" : [ 0, 1, 2 ] } }, { "key" : 2, "foo" : { "bar" : [ 3, 4, 5 ] } }, { "key" : 2, "foo" : { "bar" : [ 6, 7, 8 ] } } ], "d" : [ 2, 2, 2 ], "e" : [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ], "f" : [ [ 0, 1, 2 ], [ 3, 4, 5 ], [ 6, 7, 8 ] ] })" :)
for $i in annotate(
  for $j in 1 to 10 return {"key" : $j mod 3, "foo":{"bar":[ $j - 2 to $j ]}},
  {"key" : "integer", "foo":{"bar":["integer"]}}
)
let $j := $i.key
let $l := $i.foo.bar[]
let $m := $i.foo.bar
group by $k := $i.key
order by $k
return { "k": $k, "c" : [ $i ], "d" : [ $j ], "e" : [ $l ], "f" : [ $m ] }
