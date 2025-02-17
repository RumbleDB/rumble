(:JIQS: ShouldRun; Output="(8, 7, 6, 5, 4, 3, 2, 1, 0, -1)" :)
declare type local:mytype as {"foo":{"bar":["integer"]}};

for $i in validate type local:mytype* {
  for $j in 1 to 10 return {"foo":{"bar":[ $j - 2 to $j ]}}
}
order by $i.foo.bar[[2]] descending
return $i.foo.bar[[1]]