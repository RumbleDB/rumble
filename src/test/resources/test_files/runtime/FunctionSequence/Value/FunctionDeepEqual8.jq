(:JIQS: ShouldRun; Output="(false, false, false)" :)
deep-equal({"a": [2, 3]}, {"a": [2, 4]}),
deep-equal([{"a": 2.0}, {"b": "string"}], [{"a": 2.0}, {"c": "string"}]),
deep-equal({"a": {"c": [2, 3]}, "b": [{"d" : 4}]}, {"a": {"c": [2, 3]}, "b": [{"d" : 4}, {"e" : 5}]})


(: array/object mixed tests - not matching :)

