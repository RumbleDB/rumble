(:JIQS: ShouldRun; UpdateDim=[2,11]; Output="" :)
for $data in delta-file("./tempDeltaTable")
count $c
return
    if ($c eq 1)
    then
        insert {"is_not_1_2" : false} into $data
    else
        insert {"is_1_2" : false} into $data