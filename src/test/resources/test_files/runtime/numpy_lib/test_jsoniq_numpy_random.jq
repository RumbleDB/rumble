(:JIQS: ShouldRun; Output="(1, 3, 4, 10, 600, 21, 256)":)
import module namespace numpy = "jsoniq_numpy.jq";
count([numpy:random()]),
count(numpy:random([3])),
count(numpy:random_uniform({"low": 0.5, "high":  1.0, "size": [4]})),
count(numpy:random_randint(5, {"high":  10, "size": [10]})),
count(flatten(numpy:random_randint(5, {"high":  10, "size": [10, 20, 3]}))),
count(flatten(numpy:random([3, 7, 1, 1]))),
count(flatten(numpy:random_uniform({"low": 0.5, "high":  1.0, "size": [4, 4, 4, 4]})))