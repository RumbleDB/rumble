(:JIQS: ShouldRun; Output="(1, 3, 4, 10)":)
import module namespace numpy = "jsoniq_numpy.jq";
size([numpy:random()]),
size(numpy:random(3)),
size(numpy:random_uniform({"low": 0.5, "high":  1.0, "size": 4})),
size(numpy:random_randint(5, {"high":  10, "size": 10}))