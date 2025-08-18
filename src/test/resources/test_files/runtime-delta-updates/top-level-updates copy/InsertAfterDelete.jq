(:JIQS: ShouldRun; UpdateDim=[6,8]; Output="45" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (45 to 45) ! { "key" : $$ }}) after delta-file("upcollectdelta")[8] into collection,
delete subsequence(delta-file("upcollectdelta"), 8, 1) from collection);
delta-file("upcollectdelta")[8].key