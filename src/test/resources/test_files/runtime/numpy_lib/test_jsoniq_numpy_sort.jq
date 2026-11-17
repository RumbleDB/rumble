(:JIQS: ShouldRun; Output="([ -1, 1, 2, 6, 7, 10 ], [ ], [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24 ], [ 1, 2, 3, 4, 5, 6, 7, 10 ], [ -1, 1, 2, 3, 4, 5, 6, 7 ], [ 1, 2, 2, 3, 5, 6, 7 ], [ 1, 1, 1, 1, 1, 1 ], [ -2, -1, 1, 2 ], [ -3, -2, -1, 1, 2 ])":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:sort([6, 7, 1, 2, 10, -1]),
numpy:sort([]),
numpy:sort(numpy:arange(25)),
numpy:sort([10, 1, 2, 3, 4, 5, 6, 7]),
numpy:sort([1, 2, 3, 4, 5, 6, 7, -1]),
numpy:sort([1, 2, 3, 2, 5, 6, 7]),
numpy:sort([1, 1, 1, 1, 1, 1]),
numpy:sort([1, -1, 2, -2]),
numpy:sort([1, 2, -1, -2, -3])