(:JIQS: ShouldRun; Output="(false, false, false, false, false, false, false)" :)
deep-equal({"a": 1}, {"a": 2}),
deep-equal({"a": 1}, {"b": 1}),
deep-equal({"a": 1}, {"b": 1, "c": 2}),
deep-equal({"a": 2.0, "b": "string"}, {"a": 3e+0, "b": "string"}),
deep-equal({"a": 2.0, "b": "string"}, {"a": 2e+0, "b": "different string"}),
deep-equal({"a": "string", "b": {"c" : 2}}, {"a": "string", "b": {"d" : 2.0}}),
deep-equal({"a": "string", "b": {"c" : 2}}, {"a": "string", "b": {"c" : 3.0}})


(: obj tests - not matching :)

