(:JIQS: ShouldRun; UpdateDim=[0,12]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.string_array[[2]]