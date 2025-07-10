(:JIQS: ShouldRun; UpdateDim=[5,2]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return replace value of json $data.new_ins with "DOUBLE SUCCESS";
delta-file("./tempDeltaTable").new_ins