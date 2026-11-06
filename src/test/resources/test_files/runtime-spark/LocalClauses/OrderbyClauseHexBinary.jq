(:JIQS: ShouldRun; Output="(0123456789ABCDEF, AABB, AABB)" :)
for $j as hexBinary in (hexBinary("0123456789abcdef"), hexBinary("AaBb"), hexBinary("aAbB"), hexBinary(()))
order by $j
return $j
