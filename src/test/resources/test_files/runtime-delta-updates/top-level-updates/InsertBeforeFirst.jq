(:JIQS: ShouldRun; UpdateDim=[6,3]; Output="35" :)
(insert (31 to 31) ! { "key" : $$ } before table("upcollect")[1] into collection,
insert (21 to 21) ! { "key" : $$ } first into collection table("upcollect"));
table("upcollect")[1].key + count(table("upcollect"))
