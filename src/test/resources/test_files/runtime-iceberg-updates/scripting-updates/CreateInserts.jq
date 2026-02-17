(:JIQS: ShouldRun; UpdateDim=[10,10]; Output="({ "i" : 1 }, { "i" : 2 }, { "i" : 3 }, { "i" : 4 }, { "i" : 5 })" :)
create collection iceberg-table("icy10") with ();

insert { "i": 1 } last into collection iceberg-table("icy10");
insert { "i": 2 } last into collection iceberg-table("icy10");
insert { "i": 3 } last into collection iceberg-table("icy10");
insert { "i": 4 } last into collection iceberg-table("icy10");
insert { "i": 5 } last into collection iceberg-table("icy10");

iceberg-table("icy10")