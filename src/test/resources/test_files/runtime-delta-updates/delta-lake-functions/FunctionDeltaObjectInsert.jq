(:JIQS: ShouldRun; UpdateDim=[5,1]; Output="(SUCCESS, [ "SUCCESS" ])" :)
let $data := delta-file("./tempDeltaTable")
return (insert json "new_ins" : "SUCCESS" into $data, insert json "new_array" : ["SUCCESS"] into $data);
(delta-file("./tempDeltaTable").new_ins, delta-file("./tempDeltaTable").new_array)