(:JIQS: ShouldRun; UpdateDim=[2,8]; Output="1" :)
(delete hudi-table("hoodie")[1] from collection,
delete hudi-table("hoodie")[2] from collection,
delete hudi-table("hoodie")[3] from collection,
delete hudi-table("hoodie")[4] from collection,
delete hudi-table("hoodie")[5] from collection,
delete hudi-table("hoodie")[6] from collection,
delete hudi-table("hoodie")[7] from collection,
delete hudi-table("hoodie")[8] from collection,
delete hudi-table("hoodie")[9] from collection);
count(hudi-table("hoodie"))
