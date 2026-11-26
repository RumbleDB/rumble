(:JIQS: ShouldRun; UpdateDim=[0,2]; Output="(1, a)" :)
for $row in hudi-table("hoodietypes")
return ($row.id, $row.info.v)
