(:JIQS: ShouldRun; UpdateDim=[8,8]; Output="45" :)
(insert  (45 to 45) ! { "key" : $$ } after delta-file("upcollectdelta")[8] into collection,
delete subsequence(delta-file("upcollectdelta"), 8, 1) from collection);
delta-file("upcollectdelta")[8].key