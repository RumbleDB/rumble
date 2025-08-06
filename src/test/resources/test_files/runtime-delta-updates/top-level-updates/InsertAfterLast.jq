(:JIQS: ShouldRun; UpdateDim=[6,5]; Output="44" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (36 to 36) ! { "key" : $$ }}) after table("upcollect")[16] into collection,
insert (validate type local:t* { (26 to 26) ! { "key" : $$ }}) last into collection table("upcollect"));
table("upcollect")[18].key + count(table("upcollect"))