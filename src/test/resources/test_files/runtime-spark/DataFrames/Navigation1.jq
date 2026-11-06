(:JIQS: ShouldRun; Output="(175, 91)" :)
sum(structured-json-lines("../../../queries/denormalized.json").foo[].bar),
sum(structured-json-lines("../../../queries/denormalized.json").foo[[1]].bar)
