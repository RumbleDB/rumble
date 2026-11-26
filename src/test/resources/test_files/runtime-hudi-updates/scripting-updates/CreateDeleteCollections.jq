(:JIQS: ShouldRun; UpdateDim=[3,0]; Output="" :)
create collection hudi-table("hoodie1") with ();
create collection hudi-table("hoodie2");
create collection hudi-table("hoodie3") with ();
(delete collection hudi-table("hoodie1"),
delete collection hudi-table("hoodie2"),
delete collection hudi-table("hoodie3"));