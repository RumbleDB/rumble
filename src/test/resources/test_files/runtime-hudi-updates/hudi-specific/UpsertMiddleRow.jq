(:JIQS: ShouldRun; UpdateDim=[5,3]; Output="" :)
for $row in hudi-table("hoodiemeta")
where $row.id eq 2
return replace value of json $row.info.v with "B"
