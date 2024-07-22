(:JIQS: ShouldRun; Output="{ "a" : 1, "b" : 2, "c" : 3 }" :)
copy $je := {"a" : 1, "b" : 2}
modify if($je.c)
       then
           delete $je.c
       else
           insert "c" : 3 into $je
return $je
