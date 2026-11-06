(:JIQS: ShouldRun; UpdateDim=[2,2]; Output="(-1, -1)" :)
for $data in delta-file("./tempDeltaTable")
return $data.foo