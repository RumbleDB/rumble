(:JIQS: ShouldRun; UpdateDim=[6,2]; Output="32" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (20 to 20) ! { "key" : $$ }}) first into collection delta-file("upcollectdelta"),
insert (validate type local:t* { (30 to 30) ! { "key" : $$ }}) before delta-file("upcollectdelta")[1] into collection);
delta-file("upcollectdelta")[1].key + count(delta-file("upcollectdelta"))