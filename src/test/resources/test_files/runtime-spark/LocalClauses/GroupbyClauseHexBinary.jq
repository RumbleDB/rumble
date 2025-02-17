(:JIQS: ShouldRun; Output="(AABB, 0123456789ABCDEF)" :)
for $j as hexBinary in (hexBinary("0123456789abcdef"), hexBinary("AaBb"), hexBinary("aAbB"), hexBinary(()))
group by $j
return $j
