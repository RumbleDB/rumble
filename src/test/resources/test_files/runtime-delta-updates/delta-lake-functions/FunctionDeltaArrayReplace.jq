(:JIQS: ShouldRun; UpdateDim=[5,6]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return replace value of json $data.new_array[[1]] with "DOUBLE SUCCESS";
delta-file("./tempDeltaTable").new_array[[1]]