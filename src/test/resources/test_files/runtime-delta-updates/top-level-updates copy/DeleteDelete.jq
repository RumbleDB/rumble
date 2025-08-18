(:JIQS: ShouldRun; UpdateDim=[6,13]; Output="23" :)
(delete subsequence(delta-file("upcollectdelta"), 10, 1) from collection,
delete subsequence(delta-file("upcollectdelta"), 10, 1) from collection);
count(delta-file("upcollectdelta"))