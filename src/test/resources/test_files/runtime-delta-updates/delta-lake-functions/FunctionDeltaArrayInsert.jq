(:JIQS: ShouldRun; UpdateDim=[5,5]; Output="SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return insert json "SUCCESS" into $data.new_array at position 1;
delta-file("./tempDeltaTable").new_array[[1]]