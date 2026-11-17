(:JIQS: ShouldRun; UpdateDim=[8,5]; Output="44" :)
(insert  (36 to 36) ! { "key" : $$ } after delta-file("upcollectdelta")[16] into collection,
insert  (26 to 26) ! { "key" : $$ } last into collection delta-file("upcollectdelta"));
delta-file("upcollectdelta")[18].key + count(delta-file("upcollectdelta"))