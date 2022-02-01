(:JIQS: ShouldRun; Output="({ "key" : null, "count" : 4 }, { "key" : true, "count" : 4 }, { "key" : "foo", "count" : 3 }, { "key" : 2, "count" : 1 }, { "key" : 1, "count" : 2 }, { "key" : false, "count" : 2 }, { "key" : "bar", "count" : 1 }, { "key" : null, "count" : 2 })" :)
for $i in parallelize((
{ "a" : 1, "b" : 1 },
{ "a" : true, "b" : 1 },
{ "a" : false, "b" : 1 },
{ "a" : false, "b" : 1 },
{ "a" : null, "b" : 1 },
{ "a" : null, "b" : 1 },
{ "a" : true, "b" : 1 },
{ "a" : true, "b" : 1 },
{ "a" : true, "b" : 1 },
{ "a" : "foo", "b" : 1 },
{ "a" : "foo", "b" : 1 },
{ "a" : "foo", "b" : 1 },
{ "a" : "bar", "b" : 1 },
{ "a" : 2, "b" : 1 },
{ "a" : 1, "b" : 1 },
{ "a" : [], "b" : 1 },
{ "a" : [], "b" : 1 },
{ "a" : [], "b" : 1 },
{ "a" : [], "b" : 1 }
))
let $j := flatten($i.a)
group by $j
order by $j
return {
  "key" : $j, "count": sum($i.b)
}
