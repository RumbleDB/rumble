(:JIQS: ShouldRun; UpdateDim=[0,6]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return $data.new_ins