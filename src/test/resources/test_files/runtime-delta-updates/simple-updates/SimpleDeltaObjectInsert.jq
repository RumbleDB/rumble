(:JIQS: ShouldRun; UpdateDim=[0,3]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return insert json "new_ins" : "SUCCESS" into $data