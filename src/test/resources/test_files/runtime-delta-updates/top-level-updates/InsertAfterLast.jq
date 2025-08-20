(:JIQS: ShouldRun; UpdateDim=[6,5]; Output="44" :)
(insert (36 to 36) ! { "key" : $$ } after table("upcollect")[16] into collection,
insert (26 to 26) ! { "key" : $$ } last into collection table("upcollect"));
table("upcollect")[18].key + count(table("upcollect"))