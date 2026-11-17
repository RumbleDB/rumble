(:JIQS: ShouldRun; Output="Division by zero" :)
try {
  1 div 0
} catch err:FOAR0000 | err:FOAR0001 {
  "Division by zero"
} catch err:FOAR0002 {
  "Not division by zero"
} catch err:FOAR0003 | err:FOAR0004 {
  "Not division by zero"
} catch * {
  "Not division by zero"
}
