(:JIQS: ShouldRun; Output="(true, true, true, true, true, false, false, false, false, false)" :)
declare type local:mytype as {"foo":{"bar": "integer"}};
for $i in validate type local:mytype* {
  for $j in 1 to 10 return {"foo":{"bar":$j}}
}
let $g := $i.foo.bar le 5
return $g