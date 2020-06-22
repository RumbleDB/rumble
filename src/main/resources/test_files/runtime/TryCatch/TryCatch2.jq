(:JIQS: ShouldRun; Output="Division by zero" :)
try {
  1 div 0
} catch * {
  "Division by zero"
}
