(:JIQS: ShouldRun; Output="3" :)
variable $y := 4;
try {
  parallelize(1 to 1000) ! (1 div 0);
} catch FOAR0001 {
  $y := 3;
}
$y