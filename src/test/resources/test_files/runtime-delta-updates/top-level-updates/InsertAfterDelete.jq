(:JIQS: ShouldRun; UpdateDim=[6,8]; Output="45" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (45 to 45) ! { "key" : $$ }}) after table("upcollect")[8] into collection,
delete subsequence(table("upcollect"), 8, 1) from collection);
table("upcollect")[8].key