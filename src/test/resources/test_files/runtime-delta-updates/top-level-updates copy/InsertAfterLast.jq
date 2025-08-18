(:JIQS: ShouldRun; UpdateDim=[6,5]; Output="44" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (36 to 36) ! { "key" : $$ }}) after delta-file("upcollectdelta")[16] into collection,
insert (validate type local:t* { (26 to 26) ! { "key" : $$ }}) last into collection delta-file("upcollectdelta"));
delta-file("upcollectdelta")[18].key + count(delta-file("upcollectdelta"))