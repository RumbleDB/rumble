(:JIQS: ShouldRun; UpdateDim=[6,7]; Output="37" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (14 to 14) ! { "key" : $$ }}) before table("upcollect")[9] into collection,
insert (validate type local:t* { (15 to 15) ! { "key" : $$ }}) after table("upcollect")[8] into collection);
table("upcollect")[9].key + count(table("upcollect"))