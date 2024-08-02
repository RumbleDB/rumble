(:JIQS: ShouldRun; UpdateDim=[2,1]; Output="" :)
for $data in delta-file("./tempDeltaTable")
return replace value of json $data.foo with -1