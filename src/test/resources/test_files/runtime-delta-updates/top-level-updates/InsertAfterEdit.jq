(:JIQS: ShouldRun; UpdateDim=[6,11]; Output="51" :)
(insert (51 to 51) ! { "key" : $$ } after table("upcollect")[8] into collection,
edit table("upcollect")[8] into { "key" : 400 } in collection);
table("upcollect")[9].key