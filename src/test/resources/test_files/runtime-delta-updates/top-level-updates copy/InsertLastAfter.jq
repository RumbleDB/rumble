(:JIQS: ShouldRun; UpdateDim=[6,4]; Output="41" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (25 to 25) ! { "key" : $$ }}) last into collection delta-file("upcollectdelta"),
insert (validate type local:t* { (35 to 35) ! { "key" : $$ }}) after delta-file("upcollectdelta")[14] into collection);
delta-file("upcollectdelta")[16].key + count(delta-file("upcollectdelta"))