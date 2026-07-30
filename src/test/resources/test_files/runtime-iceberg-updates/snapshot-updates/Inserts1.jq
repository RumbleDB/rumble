(:JIQS: ShouldRun; UpdateDim=[9,5]; Output="({ "should-be-first" : 9789 }, { "index" : 1 }, { "latest" : "laster" })" :)
(insert {"should-be-first": 9789} first into collection iceberg-table("icy"),
insert {"latest": "laster"} last into collection iceberg-table("icy"));
iceberg-table("icy")
