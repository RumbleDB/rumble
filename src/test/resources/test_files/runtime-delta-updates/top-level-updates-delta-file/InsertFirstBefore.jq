(:JIQS: ShouldRun; UpdateDim=[8,2]; Output="32" :)
(insert  (20 to 20) ! { "key" : $$ } first into collection delta-file("upcollectdelta"),
insert  (30 to 30) ! { "key" : $$ } before delta-file("upcollectdelta")[1] into collection);
delta-file("upcollectdelta")[1].key + count(delta-file("upcollectdelta"))