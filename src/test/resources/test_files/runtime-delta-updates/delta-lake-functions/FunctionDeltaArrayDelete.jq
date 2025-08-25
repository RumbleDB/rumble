(:JIQS: ShouldRun; UpdateDim=[5,7]; Output="[ "SUCCESS" ]" :)
let $data := delta-file("./tempDeltaTable")
return delete json $data.new_array[[1]];
delta-file("./tempDeltaTable").new_array