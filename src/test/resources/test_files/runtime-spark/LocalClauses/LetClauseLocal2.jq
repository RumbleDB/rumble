(:JIQS: ShouldRun; Output="1000" :)
let $i := 1 to 1000 return count($i)

(: local execution - avoids truncation of spark execution :)
