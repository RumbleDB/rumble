(:JIQS: ShouldRun; Output="(5, 4, 0, 101, 1, 10, 1, 11)":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:binsearch(numpy:arange(10, {"start": 0, "step": 1}), 4),
numpy:binsearch(numpy:arange(11, {"start": 0, "step": 1}), 3),
numpy:binsearch(numpy:arange(100, {"start": 0, "step": 1}), -12),
numpy:binsearch(numpy:arange(100, {"start": 0, "step": 1}), 101),
numpy:binsearch(numpy:arange(10, {"start": 0, "step": 1}), 0),
numpy:binsearch(numpy:arange(10, {"start": 0, "step": 1}), 9),
numpy:binsearch(numpy:arange(11, {"start": 0, "step": 1}), 0),
numpy:binsearch(numpy:arange(11, {"start": 0, "step": 1}), 10)