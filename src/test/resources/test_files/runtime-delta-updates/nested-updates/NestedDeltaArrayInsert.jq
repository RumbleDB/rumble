(:JIQS: ShouldRun; UpdateDim=[1,9]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return insert {"string" : "NEW SUCCESS" } into $data.object.object.object_array at position 2