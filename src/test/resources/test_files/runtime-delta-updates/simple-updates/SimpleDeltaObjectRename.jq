(:JIQS: ShouldRun; UpdateDim=[0,7]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return rename json $data.new_ins as "success"