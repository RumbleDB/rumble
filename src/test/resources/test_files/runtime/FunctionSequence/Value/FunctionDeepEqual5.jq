(:JIQS: ShouldRun; Output="(true, true, true)" :)
deep-equal({"a": 1}, {"a": 1}),
deep-equal({"a": 2.0, "b": "string"}, {"a": 2e+0, "b": "string"}),
deep-equal({"a": "string", "b": {"c" : 2}}, {"a": "string", "b": {"c" : 2.0}})

(: object tests - matching :)

