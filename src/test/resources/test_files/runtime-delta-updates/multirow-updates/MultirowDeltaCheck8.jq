(:JIQS: ShouldRun; UpdateDim=[2,16]; Output="({ "key1" : "value1", "key2" : "value2" }, { "key1" : "value1", "key2" : "value2" })" :)
for $data in delta-file("./tempDeltaTable")
return $data.nest