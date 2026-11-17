(:JIQS: ShouldRun; UpdateDim=[6,12]; Output="500" :)
( edit table("upcollect")[8] into { "key" : 500 } in collection ,
delete subsequence(table("upcollect"), 8, 1) from collection );
table("upcollect")[8].key