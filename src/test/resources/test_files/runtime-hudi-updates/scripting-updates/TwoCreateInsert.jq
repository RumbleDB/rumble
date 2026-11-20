(:JIQS: ShouldRun; UpdateDim=[3,9]; Output="({ "c" : "A", "v" : 1 }, { "c" : "B", "v" : 10 }, { "c" : "B", "v" : 20 })" :)
create collection hudi-table("hoodie9_1") with ();
create collection hudi-table("hoodie9_2") with { "c": "B", "v": 10 };

insert { "c": "A", "v": 1 } last into collection hudi-table("hoodie9_1");
insert { "c": "B", "v": 20 } last into collection hudi-table("hoodie9_2");

hudi-table("hoodie9_1"), hudi-table("hoodie9_2")