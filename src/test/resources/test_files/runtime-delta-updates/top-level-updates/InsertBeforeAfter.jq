(:JIQS: ShouldRun; UpdateDim=[6,7]; Output="37" :)
(insert (14 to 14) ! { "key" : $$ } before table("upcollect")[9] into collection,
insert (15 to 15) ! { "key" : $$ } after table("upcollect")[8] into collection);
table("upcollect")[9].key + count(table("upcollect"))