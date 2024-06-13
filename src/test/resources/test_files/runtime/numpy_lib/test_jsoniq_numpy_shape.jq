(:JIQS: ShouldRun; Output="([ 1, 1, 2 ], [ 3, 2 ], [ 7 ], [ 1, 2, 3, 4, 5, 6, 7, 8 ])":)
import module namespace utils = "jsoniq_utils.jq";
import module namespace numpy = "jsoniq_numpy.jq";
utils:shape([[[3,4]]]), utils:shape([[1,2], [3,4], [4, 5]]), utils:shape(numpy:arange(7)), utils:shape(numpy:ones([1, 2, 3, 4, 5, 6, 7, 8]))