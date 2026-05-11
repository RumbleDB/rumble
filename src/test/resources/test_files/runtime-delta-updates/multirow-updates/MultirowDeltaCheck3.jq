(:JIQS: ShouldRun; UpdateDim=[2,6]; Output="([ "test2" ], [ "test4" ])" :)
for $data in delta-file("./tempDeltaTable")
return $data.foobar