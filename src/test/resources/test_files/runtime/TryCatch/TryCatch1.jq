(:JIQS: ShouldRun; Output="Division by zero" :)
try {
  1 div 0
} catch err:FOAR0001 {
  "Division by zero"
}
