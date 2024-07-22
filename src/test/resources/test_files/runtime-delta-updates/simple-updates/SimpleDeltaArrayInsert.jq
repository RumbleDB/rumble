(:JIQS: ShouldRun; UpdateDim=[0,9]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return insert "SUCCESS" into $data.string_array at position 2