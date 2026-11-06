(:JIQS: ShouldRun; Output="4" :)
parallelize(1 to 5) ! (if($$ eq 4) then 4 else ())

(: partially empty sequence :)
