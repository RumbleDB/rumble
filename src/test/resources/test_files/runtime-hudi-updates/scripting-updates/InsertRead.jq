(:JIQS: ShouldRun; UpdateDim=[3,2]; Output="{ "a" : 1 }":)
create collection hudi-table("hoodie2") with ();
insert { "a": 1 } last into collection hudi-table("hoodie2");
hudi-table("hoodie2")