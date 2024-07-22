(:JIQS: ShouldRun; UpdateDim=[0,14]; Output="[ "hello" ]" :)
let $data := delta-file("./tempDeltaTable")
return $data.string_array