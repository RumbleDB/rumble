(:JIQS: ShouldRun; UpdateDim=[10,0]; Output="" :)
create collection iceberg-table("icy1") with ();
create collection iceberg-table("icy2");
create collection iceberg-table("icy3") with ();
(delete collection iceberg-table("icy1"),
delete collection iceberg-table("icy2"),
delete collection iceberg-table("icy3"));