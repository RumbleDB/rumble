(:JIQS: ShouldRun; Output="{ "a" : 1, "b" : 2, "c" : 3 }" :)
exit returning copy $je := {"a" : 1, "b" : 2}
                   modify if($je.c)
                          then
                              delete json $je.c
                          else
                              insert json "c" : 3 into $je
                   return $je;