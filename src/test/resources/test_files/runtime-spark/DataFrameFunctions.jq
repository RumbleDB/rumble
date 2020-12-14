(:JIQS: ShouldRun; Output="(27, 29, 27, 28, 23, 1, 28, 35)" :)
count(tail(structured-json-file("../../queries/denormalized.json"))),
count(insert-before(structured-json-file("../../queries/denormalized.json"), 1, [ "foo" ])),
count(remove(structured-json-file("../../queries/denormalized.json"), 2)),
count(reverse(structured-json-file("../../queries/denormalized.json"))),
count(subsequence(structured-json-file("../../queries/denormalized.json"), 5)),
count(subsequence(structured-json-file("../../queries/denormalized.json"), 5, 1)),
count(one-or-more(structured-json-file("../../queries/denormalized.json"))),
structured-json-file("../../queries/denormalized.json").foo[]

