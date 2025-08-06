(:JIQS: ShouldRun; UpdateDim=[6,4]; Output="41" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (25 to 25) ! { "key" : $$ }}) last into collection table("upcollect"),
insert (validate type local:t* { (35 to 35) ! { "key" : $$ }}) after table("upcollect")[14] into collection);
table("upcollect")[16].key + count(table("upcollect"))