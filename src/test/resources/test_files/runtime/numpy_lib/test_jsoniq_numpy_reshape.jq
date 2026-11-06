(:JIQS: ShouldRun; Output="([ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ], [ [ [ [ [ 0, 0 ], [ 0, 0 ], [ 0, 0 ] ] ] ] ], [ [ 0, 0 ], [ 0, 0 ], [ 0, 0 ] ], [ [ [ [ 0 ] ], [ [ 0 ] ] ], [ [ [ 0 ] ], [ [ 0 ] ] ], [ [ [ 0 ] ], [ [ 0 ] ] ] ], [ [ [ 1, 4, 2 ] ], [ [ 241, -2, 34 ] ], [ [ 432, 234, 1 ] ], [ [ 423, 43, 430 ] ] ])":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:reshape(numpy:zeros([5,6]), [30]),
numpy:reshape(numpy:zeros([1,2,3]), [1, 1, 1, 3, 2]),
numpy:reshape(numpy:zeros([1,2,3]), [3, 2]),
numpy:reshape(numpy:zeros([1,2,3]), [3, 2, 1, 1]),
numpy:reshape([1, 4, 2, 241, -2, 34, 432, 234, 1, 423, 43, 430], [4, 1, 3])