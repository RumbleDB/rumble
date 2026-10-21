(:JIQS: ShouldRun; Output="(3, 2, 1)" :)
declare function local:objects($n as integer) as object* {
  for $i in parallelize(1 to $n) return { "i" : $i }
};
for $object in local:objects(3)
order by $object.i descending
return $object.i

(: A function returning a DataFrame. :)
