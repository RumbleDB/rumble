for $i in parallelize(({"id": 1}, {"id": 2})) 
let $n := 5 
where $i.id < $n 
return $i