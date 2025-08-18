(:JIQS: ShouldRun; UpdateDim=[6,10]; Output="50" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (50 to 50) ! { "key" : $$ }}) before delta-file("upcollectdelta")[8] into collection,
edit delta-file("upcollectdelta")[8] into (validate type local:t* { { "key" : 300 }, {"key" : 301 }})[position() gt 1] from collection);
delta-file("upcollectdelta")[8].key