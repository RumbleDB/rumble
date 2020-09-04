(:JIQS: ShouldRun; Output="Division by zero" :)
try {
  1 div 0
} catch FOAR0000 | FOAR0001 {
  "Division by zero"
} catch FOAR0002 {
  "Not division by zero"
} catch FOAR0003 | FOAR0004 {
  "Not division by zero"
} catch * {
  "Not division by zero"
}
