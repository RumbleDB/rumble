(:JIQS: ShouldRun; UpdateDim=[2,5]; Output="({ "should_be_first" : 9789 }, { "index" : 1 }, { "latest" : "laster" })" :)
(insert {"should_be_first": 9789} first into collection hudi-table("hoodie"),
insert {"latest": "laster"} last into collection hudi-table("hoodie"));
hudi-table("hoodie")
