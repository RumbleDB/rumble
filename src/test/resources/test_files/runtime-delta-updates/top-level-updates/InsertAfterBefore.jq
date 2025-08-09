(:JIQS: ShouldRun; UpdateDim=[6,6]; Output="32" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (12 to 12) ! { "key" : $$ }}) after table("upcollect")[8] into collection,
insert (validate type local:t* { (13 to 13) ! { "key" : $$ }}) before table("upcollect")[9] into collection);
table("upcollect")[9].key + count(table("upcollect"))