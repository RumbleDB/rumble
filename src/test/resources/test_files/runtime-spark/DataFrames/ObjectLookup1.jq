declare variable $f := "(1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7, 1, 5, 7)";
for $i in structured-json-file("../../../queries/denormalized.json").foo[[1]].($f) return $i