(:JIQS: ShouldRun; Output="(175, 91)" :)
sum(structured-json-file("../../../queries/denormalized.json").foo[].bar),
sum(structured-json-file("../../../queries/denormalized.json").foo[[1]].bar)
