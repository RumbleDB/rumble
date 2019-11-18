(:JIQS: ShouldRun; Output="(0123456789ABCDEF, AABB)" :)
for $j as hexBinary in (hexBinary("0123456789abcdef"), hexBinary("AaBb"), hexBinary("aAbB"), hexBinary(()))
group by $j
return $j