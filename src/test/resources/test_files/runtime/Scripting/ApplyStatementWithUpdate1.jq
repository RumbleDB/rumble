(:JIQS: ShouldRun; Output="[ 1, 3, 4 ]" :)
variable $je := [1 to 4];
delete json $je[[2]];
$je