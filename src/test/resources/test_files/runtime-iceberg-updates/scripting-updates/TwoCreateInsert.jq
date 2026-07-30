(:JIQS: ShouldRun; UpdateDim=[10,9]; Output="({ "c" : "A", "v" : 1 }, { "c" : "B", "v" : 10 }, { "c" : "B", "v" : 20 })" :)
create collection iceberg-table("icy9_1") with ();
create collection iceberg-table("icy9_2") with { "c": "B", "v": 10 };

insert { "c": "A", "v": 1 } last into collection iceberg-table("icy9_1");
insert { "c": "B", "v": 20 } last into collection iceberg-table("icy9_2");

iceberg-table("icy9_1"), iceberg-table("icy9_2")