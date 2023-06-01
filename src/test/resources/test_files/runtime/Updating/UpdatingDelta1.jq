(:JIQS: ShouldRun; Output="[ 3, 4 ]" :)
let $data := delta-file("../../../queries/sample_updating_delta")
return delete $data.bool
