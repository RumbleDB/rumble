(:JIQS: ShouldRun; Output="10" :)
declare type local:mytype as {"foo":{"bar":["integer"]}};
for $i in validate type local:mytype* {
  for $j in 1 to 10 return {"foo":{"bar":[ 1 to $j ]}}
}
let $g := $i.foo
let $h := $g.bar
let $j := $h[[1]]
group by $j
return count($i)