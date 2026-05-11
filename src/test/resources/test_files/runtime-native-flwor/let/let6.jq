(:JIQS: ShouldRun; Output="(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)" :)
declare type local:mytype as {"foo":{"bar": "integer"}};
for $i in validate type local:mytype* {
  for $j in 1 to 10 return {"foo":{"bar":$j}}
}
let $g := $i.foo.bar
return $g