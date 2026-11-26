(:JIQS: ShouldRun; UpdateDim=[1,14]; Output="[ { "string" : "hello" } ]" :)
let $data := hudi-table("tempHudiTable")
return $data.object.object.object_array