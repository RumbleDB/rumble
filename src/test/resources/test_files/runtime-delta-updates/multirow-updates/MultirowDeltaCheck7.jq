(:JIQS: ShouldRun; UpdateDim=[2,14]; Output="({ "key1" : "value1" }, { "key1" : "value1" })" :)
for $data in delta-file("./tempDeltaTable")
return $data.nest