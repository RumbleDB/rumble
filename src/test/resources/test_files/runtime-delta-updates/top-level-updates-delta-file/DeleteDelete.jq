(:JIQS: ShouldRun; UpdateDim=[8,13]; Output="23" :)
(delete subsequence(delta-file("upcollectdelta"), 10, 1) from collection,
delete subsequence(delta-file("upcollectdelta"), 10, 1) from collection);
count(delta-file("upcollectdelta"))