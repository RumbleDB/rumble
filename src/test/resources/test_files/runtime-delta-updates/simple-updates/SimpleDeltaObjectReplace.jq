(:JIQS: ShouldRun; UpdateDim=[0,5]; Output="" :)
let $data := delta-file("./tempDeltaTable")
return replace value of $data.new_ins with "DOUBLE SUCCESS"