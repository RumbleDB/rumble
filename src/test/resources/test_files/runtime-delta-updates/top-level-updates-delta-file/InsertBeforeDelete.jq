(:JIQS: ShouldRun; UpdateDim=[8,9]; Output="46" :)
(insert  (46 to 46) ! { "key" : $$ } before delta-file("upcollectdelta")[8] into collection,
delete subsequence(delta-file("upcollectdelta"), 7, 1) from collection);
delta-file("upcollectdelta")[7].key