(:JIQS: ShouldRun; UpdateDim=[4,1]; Output="{ "ok" : 1, "nested" : { "inner" : 2 } }" :)
create collection hudi-table("hoodiekeys") with { "ok" : 1, "nested" : { "inner" : 2 } };
hudi-table("hoodiekeys")
