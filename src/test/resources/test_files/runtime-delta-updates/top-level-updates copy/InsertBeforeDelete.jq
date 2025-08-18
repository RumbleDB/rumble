(:JIQS: ShouldRun; UpdateDim=[6,9]; Output="46" :)
declare type local:t as { "key" : "integer" };
(insert (validate type local:t* { (46 to 46) ! { "key" : $$ }}) before delta-file("upcollectdelta")[8] into collection,
delete subsequence(delta-file("upcollectdelta"), 7, 1) from collection);
delta-file("upcollectdelta")[7].key