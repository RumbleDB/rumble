(:JIQS: ShouldRun; UpdateDim=[8,6]; Output="32" :)
(insert (12 to 12) ! { "key" : $$ } after delta-file("upcollectdelta")[8] into collection,
insert (13 to 13) ! { "key" : $$ } before delta-file("upcollectdelta")[9] into collection);
delta-file("upcollectdelta")[9].key + count(delta-file("upcollectdelta"))