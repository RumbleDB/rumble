(:JIQS: ShouldRun; UpdateDim=[6,4]; Output="41" :)
(insert (25 to 25) ! { "key" : $$ } last into collection table("upcollect"),
insert (35 to 35) ! { "key" : $$ } after table("upcollect")[14] into collection);
table("upcollect")[16].key + count(table("upcollect"))