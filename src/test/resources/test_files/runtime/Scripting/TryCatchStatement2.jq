(:JIQS: ShouldRun; Output="Division by zero" :)
try {
  parallelize(1 to 1000) ! (1 div 0);
} catch FOAR0001 {
  "Division by zero";
}
"Division by zero"
