(:JIQS: ShouldRun; Output="(3, 4, 4, 3, [ 1, 2 ], [ 2, 0, 1 ], 8, [ [ 0, 1, 0, 1 ], [ 1, 0, 1, 1 ], [ 1, 1, 1, 0 ] ], [ [ 2, 2, 2, 2 ] ], [ [ 2, 3, 3 ] ])":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:count_nonzero([1,2,3]),
numpy:count_nonzero([-1,2,-3, 0, 1, 0]),
numpy:count_nonzero([-1,2,-3, 0, 1, 0], {"axis": 0}),
numpy:count_nonzero([[-1, 0, 0], [2, 0, -3]]),
numpy:count_nonzero([[-1, 0, 0], [2, 0, -3]], {"axis": 1}),
numpy:count_nonzero([[-1, 0, 0], [2, 0, -3]], {"axis": 0}),
numpy:count_nonzero([[[0, 1, 0, 3], [2, 0, -3, 7], [2, 1, -3, 0]]]),
numpy:count_nonzero([[[0, 1, 0, 3], [2, 0, -3, 7], [2, 1, -3, 0]]], {"axis": 0}),
numpy:count_nonzero([[[0, 1, 0, 3], [2, 0, -3, 7], [2, 1, -3, 0]]], {"axis": 1}),
numpy:count_nonzero([[[0, 1, 0, 3], [2, 0, -3, 7], [2, 1, -3, 0]]], {"axis": 2})
(: numpy:count_nonzero(["1", "0", "2"]),
numpy:count_nonzero([[true, false], [false, false]]) :)