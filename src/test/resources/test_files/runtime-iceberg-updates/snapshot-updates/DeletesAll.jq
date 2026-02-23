(:JIQS: ShouldRun; UpdateDim=[9,8]; Output="1" :)
(delete iceberg-table("icy")[1] from collection,
delete iceberg-table("icy")[2] from collection,
delete iceberg-table("icy")[3] from collection,
delete iceberg-table("icy")[4] from collection,
delete iceberg-table("icy")[5] from collection,
delete iceberg-table("icy")[6] from collection,
delete iceberg-table("icy")[7] from collection,
delete iceberg-table("icy")[8] from collection,
delete iceberg-table("icy")[9] from collection);
count(iceberg-table("icy"))
