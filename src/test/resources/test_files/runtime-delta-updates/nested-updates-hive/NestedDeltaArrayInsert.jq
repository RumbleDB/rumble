(:JIQS: ShouldRun; UpdateDim=[7,9]; Output="" :)
let $data := table("tempDeltaTable")
return insert json {"string" : "NEW SUCCESS" } into $data.object.object.object_array at position 2