(:JIQS: ShouldRun; UpdateDim=[3,10]; Output="({ "i" : 1 }, { "i" : 2 }, { "i" : 3 }, { "i" : 4 }, { "i" : 5 })" :)
create collection hudi-table("hoodie10") with ();

insert { "i": 1 } last into collection hudi-table("hoodie10");
insert { "i": 2 } last into collection hudi-table("hoodie10");
insert { "i": 3 } last into collection hudi-table("hoodie10");
insert { "i": 4 } last into collection hudi-table("hoodie10");
insert { "i": 5 } last into collection hudi-table("hoodie10");

hudi-table("hoodie10")