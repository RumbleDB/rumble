(:JIQS: ShouldRun; UpdateDim=[5,8]; Output="true" :)
let $ret := delete-delta-lake-table("./tempDeltaTable")
return $ret