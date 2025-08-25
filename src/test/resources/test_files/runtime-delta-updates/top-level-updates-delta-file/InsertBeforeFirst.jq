(:JIQS: ShouldRun; UpdateDim=[8,3]; Output="35" :)
(insert  (31 to 31) ! { "key" : $$ } before delta-file("upcollectdelta")[1] into collection,
insert  (21 to 21) ! { "key" : $$ } first into collection delta-file("upcollectdelta"));
delta-file("upcollectdelta")[1].key + count(delta-file("upcollectdelta"))
