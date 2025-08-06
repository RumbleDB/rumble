(:JIQS: ShouldRun; UpdateDim=[6,11]; Output="51" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (51 to 51) ! { "key" : $$ }}) after table("upcollect")[8] into collection,
edit table("upcollect")[8] by (validate type local:t* { { "key" : 400 }, {"key" : 401 }})[position() lt 2] from collection);
table("upcollect")[9].key