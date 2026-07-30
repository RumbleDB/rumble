(:JIQS: ShouldRun; Output="4" :)
variable $y := 4;
try {
  parallelize(1 to 1000) ! (1 div 0);
} catch FOAR0001 {
  variable $y := 2;
}
$y