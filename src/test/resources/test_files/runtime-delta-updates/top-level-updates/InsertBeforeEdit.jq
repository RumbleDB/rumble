(:JIQS: ShouldRun; UpdateDim=[6,10]; Output="50" :)
(insert (50 to 50) ! { "key" : $$ } before table("upcollect")[8] into collection,
edit table("upcollect")[8] into { "key" : 300 } from collection);
table("upcollect")[8].key