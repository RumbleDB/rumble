(:JIQS: ShouldRun; UpdateDim=[2,10]; Output="({ "is_1_2" : true, "is_not_1_2" : null }, { "is_1_2" : null, "is_not_1_2" : true })" :)
for $data in delta-file("./tempDeltaTable")
return {"is_1_2" : $data.is_1_2, "is_not_1_2" : $data.is_not_1_2}