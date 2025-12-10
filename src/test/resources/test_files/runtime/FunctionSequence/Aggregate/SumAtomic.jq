(:JIQS: ShouldRun; Output="{ "quantity" : 113 }" :)

let $df := parallelize(({
  "l_quantity": 17,
  "l_returnflag": "N"
},
{
  "l_quantity": 36,
  "l_returnflag": "N"
},
{
  "l_quantity": 8,
  "l_returnflag": "N"
},
{
  "l_quantity": 28,
  "l_returnflag": "N"
},
{
  "l_quantity": 24,
  "l_returnflag": "N"
},
{
  "l_quantity": 32,
  "l_returnflag": "N"
}))

for $i in 1 to 5
let $l := $df[$i]
group by $returnflag := 0
return {"quantity": sum($l.l_quantity)}
