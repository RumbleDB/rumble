(:JIQS: ShouldRun; UpdateDim=[6,2]; Output="32" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (20 to 20) ! { "key" : $$ }}) first into collection table("upcollect"),
insert (validate type local:t* { (30 to 30) ! { "key" : $$ }}) before table("upcollect")[1] into collection);
table("upcollect")[1].key + count(table("upcollect"))