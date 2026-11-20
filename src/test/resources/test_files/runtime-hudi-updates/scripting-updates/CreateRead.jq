(:JIQS: ShouldRun; UpdateDim=[3,1]; Output="{ "x" : 1 }" :)
create collection hudi-table("hoodie1") with { "x": 1 };
hudi-table("hoodie1")