(:JIQS: ShouldRun; UpdateDim=[8,11]; Output="51" :)
(insert  (51 to 51) ! { "key" : $$ } after delta-file("upcollectdelta")[8] into collection,
edit delta-file("upcollectdelta")[8] into { "key" : 400 } in collection);
delta-file("upcollectdelta")[9].key