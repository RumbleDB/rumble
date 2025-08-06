(:JIQS: ShouldRun; UpdateDim=[6,12]; Output="500" :)
( edit table("upcollect")[8] by (validate type local:t* { { "key" : 500 }, {"key" : 501 }})[position() lt 2] from collection ,
delete subsequence(table("upcollect"), 8, 1) from collection );
table("upcollect")[8].key