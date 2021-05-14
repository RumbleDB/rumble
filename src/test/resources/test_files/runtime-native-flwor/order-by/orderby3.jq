(:JIQS: ShouldRun; Output="(-1, 0, 1, 2, 3, 4, 5, 6, 7, 8)" :)
declare type local:mytype as {"foo":{"bar":["integer"]}};

for $i in validate type local:mytype* {
  for $j in 1 to 10 return {"foo":{"bar":[ $j - 2 to $j ]}}
}
order by 1, $i.foo.bar[[2]]
return $i.foo.bar[[1]]