(:JIQS: ShouldRun; UpdateDim=[5,0]; Output="true" :)
let $ret := create-delta-lake-table("./tempDeltaTable")
return $ret