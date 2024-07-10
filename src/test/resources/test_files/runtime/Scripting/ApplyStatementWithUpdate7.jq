(:JIQS: ShouldRun; Output="[ 3, 4 ]" :)
variable $je := [1 to 4];
((), delete json $je[[1]], delete json $je[[2]]);
$je