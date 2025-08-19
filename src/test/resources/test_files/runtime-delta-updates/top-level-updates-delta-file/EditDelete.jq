(:JIQS: ShouldRun; UpdateDim=[8,12]; Output="500" :)
( edit delta-file("upcollectdelta")[8] into { "key" : 500 } from collection ,
delete subsequence(delta-file("upcollectdelta"), 8, 1) from collection );
delta-file("upcollectdelta")[8].key