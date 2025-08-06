(:JIQS: ShouldRun; UpdateDim=[6,10]; Output="50" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (50 to 50) ! { "key" : $$ }}) before table("upcollect")[8] into collection,
edit table("upcollect")[8] by (validate type local:t* { { "key" : 300 }, {"key" : 301 }})[position() gt 1] from collection);
table("upcollect")[8].key