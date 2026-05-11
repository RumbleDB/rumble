(:JIQS: ShouldRun; UpdateDim=[2,8]; Output="({ "is_1" : true, "is_not_1" : false }, { "is_1" : false, "is_not_1" : true })" :)
for $data in delta-file("./tempDeltaTable")
return {"is_1" : $data.is_1, "is_not_1" : $data.is_not_1}