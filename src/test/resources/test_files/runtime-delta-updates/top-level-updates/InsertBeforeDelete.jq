(:JIQS: ShouldRun; UpdateDim=[6,9]; Output="46" :)
(insert (46 to 46) ! { "key" : $$ } before table("upcollect")[8] into collection,
delete subsequence(table("upcollect"), 7, 1) from collection);
table("upcollect")[7].key