(:JIQS: ShouldRun; UpdateDim=[5,3]; Output="DOUBLE SUCCESS" :)
let $data := delta-file("./tempDeltaTable")
return rename json $data.new_ins as "success";
delta-file("./tempDeltaTable").success