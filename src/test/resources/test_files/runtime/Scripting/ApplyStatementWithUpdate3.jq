(:JIQS: ShouldRun; Output="[ 1, 2, 3, 5, 4, 6 ]" :)
variable $je := [1 to 4];
(insert json 5 into $je at position 4, insert json 6 into $je at position 5);
$je