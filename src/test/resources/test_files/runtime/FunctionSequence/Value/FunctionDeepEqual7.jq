(:JIQS: ShouldRun; Output="(true, true, true)" :)
deep-equal({"a": [2, 3]}, {"a": [2, 3]}),
deep-equal([{"a": 2.0}, {"b": "string"}], [{"a": 2.0}, {"b": "string"}]),
deep-equal({"a": {"c": [2, 3]}, "b": [{"d" : 4}]}, {"a": {"c": [2, 3]}, "b": [{"d" : 4}]})

(: array/object mixed tests - matching :)

