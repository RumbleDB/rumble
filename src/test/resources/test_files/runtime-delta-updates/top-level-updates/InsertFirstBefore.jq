(:JIQS: ShouldRun; UpdateDim=[6,2]; Output="32" :)
(insert (20 to 20) ! { "key" : $$ } first into collection table("upcollect"),
insert (30 to 30) ! { "key" : $$ } before table("upcollect")[1] into collection);
table("upcollect")[1].key + count(table("upcollect"))