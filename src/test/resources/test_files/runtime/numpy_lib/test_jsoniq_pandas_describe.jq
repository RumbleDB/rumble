(:JIQS: ShouldRun; Output="":)
import module namespace pandas = "jsoniq_pandas.jq";

pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}),
pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}, {"include": "number"}),
pandas:describe({"categorical": ["d", "e", "f"],"numeric": [1, 2, 3],"object": ["a", "b", "c"]}, {"include": "object"}),
pandas:describe({"0": [10, 18, 11],"1": [13, 15, 8], "2": [9, 20, 3]}),
pandas:describe({"Normal": [1, 2, 3, 4, 5],"Uniform": [1, 1, 1, 1, 1],"Skewed": [1, 1, 1, 1,100]})
