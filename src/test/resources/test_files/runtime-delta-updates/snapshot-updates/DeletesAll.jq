(:JIQS: ShouldRun; UpdateDim=[9,8]; Output="1" :)
(delete delta-file("./dfile")[1] from collection,
delete delta-file("./dfile")[2] from collection,
delete delta-file("./dfile")[3] from collection,
delete delta-file("./dfile")[4] from collection,
delete delta-file("./dfile")[5] from collection,
delete delta-file("./dfile")[6] from collection,
delete delta-file("./dfile")[7] from collection,
delete delta-file("./dfile")[8] from collection,
delete delta-file("./dfile")[9] from collection);
count(delta-file("./dfile"))
