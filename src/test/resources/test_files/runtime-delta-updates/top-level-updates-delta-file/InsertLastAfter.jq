(:JIQS: ShouldRun; UpdateDim=[8,4]; Output="41" :)
(insert  (25 to 25) ! { "key" : $$ } last into collection delta-file("upcollectdelta"),
insert  (35 to 35) ! { "key" : $$ } after delta-file("upcollectdelta")[14] into collection);
delta-file("upcollectdelta")[16].key + count(delta-file("upcollectdelta"))