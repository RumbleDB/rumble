(:JIQS: ShouldRun; UpdateDim=[2,4]; Output="({ "is_1" : true, "is_not_1" : null }, { "is_1" : null, "is_not_1" : true })" :)
for $data in delta-file("./tempDeltaTable")
return {"is_1" : $data.is_1, "is_not_1" : $data.is_not_1}