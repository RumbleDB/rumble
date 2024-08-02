(:JIQS: ShouldRun; UpdateDim=[0,4]; Output="SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.new_ins