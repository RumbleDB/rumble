(:JIQS: ShouldRun; UpdateDim=[2,4]; Output="{ "index" : 1 }" :)
(create collection hudi-table("hoodie") with { "index" : 1 });
hudi-table("hoodie")