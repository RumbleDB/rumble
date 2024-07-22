(:JIQS: ShouldRun; UpdateDim=[2,15]; Output="" :)
for $data in delta-file("./tempDeltaTable")
return insert "key2" : "value2" into $data.nest