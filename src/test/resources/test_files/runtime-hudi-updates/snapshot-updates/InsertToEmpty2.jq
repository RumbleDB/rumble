(:JIQS: ShouldRun; UpdateDim=[2,3]; Output="({ "FIRST" : 0.1 }, { "LAST" : 10 })" :)
(insert { "LAST" : 10 } last into collection hudi-table("hoodieEmpty"),
insert { "FIRST" : 0.1 } first into collection hudi-table("hoodieEmpty"));
hudi-table("hoodieEmpty")