(:JIQS: ShouldRun; Output="([ 2, 5, 4, 3 ], [ 3, 4, 5, 6, 7, 8, 9 ], [ 2, 3, 3, 3, 3, 3, 3 ], [ 0, 2, 2, 2, 5 ], [ ], [ ], [ 0, 0 ], [ 5, 2, 3, 4 ], [ 0, 0, 0, 0, 0, 0, 0 ], [ 5, 4, 4, 4, 4, 4, 4 ], [ 5, 4, 4, 4, 1 ])":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:digitize([0.2, 6.4, 3.0, 1.6], [0.0, 1.0, 2.5, 4.0, 10.0]),
numpy:digitize([1, 2, 3, 4, 5, 6, 7], numpy:arange(25)),
numpy:digitize([1, 2, 3, 4, 5, 6, 7], [1, 2, 10, 20, 30]),
numpy:digitize([1, 2, 3, 4, 7], [2, 5, 6, 7]),
numpy:digitize([], []),
numpy:digitize([], [1, 2, 3]),
numpy:digitize([1, 2], []),
numpy:digitize([0.2, 6.4, 3.0, 1.6], [10.0, 4.0, 2.5, 1.0, 0.0]),
numpy:digitize([1, 2, 3, 4, 5, 6, 7], numpy:arange(0, {"start": 25, "step": -1})),
numpy:digitize([1, 2, 3, 4, 5, 6, 7], [30, 20, 10, 2, 1]),
numpy:digitize([1, 2, 3, 4, 7], [7, 6, 5, 2])