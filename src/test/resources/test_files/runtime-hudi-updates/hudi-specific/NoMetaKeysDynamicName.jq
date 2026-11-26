(:JIQS: ShouldRun; UpdateDim=[5,2]; Output="0" :)
let $name := "hoodie" || "meta"
return count(hudi-table($name)._hoodie_commit_time)
