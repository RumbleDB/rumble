(:JIQS: ShouldRun; UpdateDim=[6,6]; Output="32" :)
(insert (12 to 12) ! { "key" : $$ } after table("upcollect")[8] into collection,
insert (13 to 13) ! { "key" : $$ } before table("upcollect")[9] into collection);
table("upcollect")[9].key + count(table("upcollect"))