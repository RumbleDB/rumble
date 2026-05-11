(:JIQS: ShouldRun; UpdateDim=[8,7]; Output="37" :)
(insert  (14 to 14) ! { "key" : $$ } before delta-file("upcollectdelta")[9] into collection,
insert  (15 to 15) ! { "key" : $$ } after delta-file("upcollectdelta")[8] into collection);
delta-file("upcollectdelta")[9].key + count(delta-file("upcollectdelta"))
