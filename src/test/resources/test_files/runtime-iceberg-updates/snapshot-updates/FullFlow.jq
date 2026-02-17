(:JIQS: ShouldRun; UpdateDim=[9,10]; Output="({ "index" : 1000 }, { "index" : 10 })" :)
(insert { "index" : 10 } last into collection iceberg-table("icy"),
delete iceberg-table("icy")[1] from collection,
edit iceberg-table("icy")[1] into { "index" : 1000 } in collection);
iceberg-table("icy")

