(:JIQS: ShouldRun; UpdateDim=[2,13]; Output="" :)
for $data in delta-file("./tempDeltaTable")
return insert json { "nest" : { "key1" : "value1" } } into $data