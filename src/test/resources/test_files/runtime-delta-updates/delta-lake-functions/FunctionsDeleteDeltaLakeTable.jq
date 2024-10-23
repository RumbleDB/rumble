(:JIQS: ShouldRun; UpdateDim=[5,0]; Output="" :)
create-delta-lake-table("./tempDeltaTable");
let $data := delta-file("./tempDeltaTable")
return $data