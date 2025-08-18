(:JIQS: ShouldRun; UpdateDim=[6,12]; Output="500" :)
declare type local:t as { "key" : "integer" };
( edit delta-file("upcollectdelta")[8] into (validate type local:t* { { "key" : 500 }, {"key" : 501 }})[position() lt 2] from collection ,
delete subsequence(delta-file("upcollectdelta"), 8, 1) from collection );
delta-file("upcollectdelta")[8].key