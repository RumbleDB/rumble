(:JIQS: ShouldRun; UpdateDim=[8,10]; Output="50" :)
(insert  (50 to 50) ! { "key" : $$ } before delta-file("upcollectdelta")[8] into collection,
edit delta-file("upcollectdelta")[8] into  { "key" : 300 } in collection);
delta-file("upcollectdelta")[8].key