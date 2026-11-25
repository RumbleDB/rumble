(:JIQS: ShouldRun; Output="({ "k" : 0, "s" : 12 }, { "k" : 1, "s" : 8 })" :)
declare type local:t as { "key" : "integer", "v" : "integer" };
for $i in validate type local:t* { for $j in 1 to 4 return { "key" : $j mod 2, "v" : $j } }
group by $k := $i.key
order by $k
return { "k" : $k, "s" : sum(for $x in $i return $x.v * 2) }
