(:JIQS: ShouldRun; UpdateDim=[3,2]; Output="{ "a" : 1 }":)
create collection iceberg-table("icy2") with ();
insert { "a": 1 } last into collection iceberg-table("icy2");
iceberg-table("icy2")