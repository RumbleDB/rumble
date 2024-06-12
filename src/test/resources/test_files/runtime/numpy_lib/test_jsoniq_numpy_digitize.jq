(:JIQS: ShouldRun; Output="([ 2, 5, 4, 3 ])":)
import module namespace numpy = "jsoniq_numpy.jq";
numpy:digitize([0.2, 6.4, 3.0, 1.6], [0.0, 1.0, 2.5, 4.0, 10.0])