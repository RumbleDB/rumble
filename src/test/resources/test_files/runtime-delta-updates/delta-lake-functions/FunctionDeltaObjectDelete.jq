(:JIQS: ShouldRun; UpdateDim=[5,4]; Output="null" :)
let $data := delta-file("./tempDeltaTable")
return delete json $data.success;
delta-file("./tempDeltaTable").success