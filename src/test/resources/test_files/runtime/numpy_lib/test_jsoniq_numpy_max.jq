(:JIQS: ShouldRun; Output="([ 2, 3 ], [ 1, 3 ], [ 2, 5, 8 ], [ 6, 7, 8 ], [ [ 4, 5, 6, 7 ], [ 12, 13, 14, 15 ] ], [ [ 8, 9, 10, 11 ], [ 12, 13, 14, 15 ] ], [ [ 3, 7 ], [ 11, 15 ] ], [ 15, 17, 25 ], [ 25, 22 ], 19, 19, 20, 19)":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:max([[0, 1], [2, 3]], {"axis": 0}),
numpy:max([[0, 1], [2, 3]], {"axis": 1}),
numpy:max([[0, 1, 2],[3, 4, 5], [6, 7, 8]], {"axis": 1}),
numpy:max([[0, 1, 2],[3, 4, 5], [6, 7, 8]], {"axis": 0}),
numpy:max([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 1}),
numpy:max([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 0}),
numpy:max([[[ 0,  1,  2,  3],[ 4,  5,  6,  7]],[[ 8,  9, 10, 11],[12, 13, 14, 15]]], {"axis": 2}),
numpy:max([[10, 17, 25], [15, 11, 22]], {"axis": 0}),
numpy:max([[10, 17, 25], [15, 11, 22]], {"axis": 1}),
numpy:max(numpy:arange(20, {"start":5})),
numpy:max(numpy:arange(20, {"start":5}), {"axis": 0}),
numpy:max(numpy:arange(20, {"start":5}), {"axis": 0, "initial": 20}),
numpy:max(numpy:arange(20, {"start":5}), { "initial": 0})