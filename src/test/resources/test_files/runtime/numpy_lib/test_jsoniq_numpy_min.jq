(:JIQS: ShouldRun; Output="([ 0, 1 ], [ 0, 2 ], [ 0, 3, 6 ], [ 0, 1, 2 ], [ [ 0, 1, 2, 3 ], [ 8, 9, 10, 11 ] ], [ [ 0, 1, 2, 3 ], [ 4, 5, 6, 7 ] ], [ [ 0, 4 ], [ 8, 12 ] ], [ 10, 11, 22 ], [ 10, 11 ], 5, 5, 0, 0)":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:min([[0, 1], [2, 3]], {"axis": 0}),
numpy:min([[0, 1], [2, 3]], {"axis": 1}),
numpy:min([[0, 1, 2],[3, 4, 5], [6, 7, 8]], {"axis": 1}),
numpy:min([[0, 1, 2],[3, 4, 5], [6, 7, 8]], {"axis": 0}),
numpy:min([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 1}),
numpy:min([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 0}),
numpy:min([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 2}),
numpy:min([[10, 17, 25], [15, 11, 22]], {"axis": 0}),
numpy:min([[10, 17, 25], [15, 11, 22]], {"axis": 1}),
numpy:min(numpy:arange(20, {"start":5})),
numpy:min(numpy:arange(20, {"start":5}), {"axis": 0}),
numpy:min(numpy:arange(20, {"start":5}), {"axis": 0, "initial": 0}),
numpy:min(numpy:arange(20, {"start":5}), { "initial": 0})