(:JIQS: ShouldRun; Output="(1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7)" :)
declare variable $f := "bar";
for $i in structured-json-file("../../../queries/denormalized.json").foo[[1]].($f) return $i