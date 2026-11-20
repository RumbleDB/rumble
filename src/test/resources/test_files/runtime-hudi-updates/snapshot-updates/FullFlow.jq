(:JIQS: ShouldRun; UpdateDim=[2,10]; Output="({ "index" : 1000 }, { "index" : 10 })" :)
(insert { "index" : 10 } last into collection hudi-table("hoodie"),
delete hudi-table("hoodie")[1] from collection,
edit hudi-table("hoodie")[1] into { "index" : 1000 } in collection);
hudi-table("hoodie")

