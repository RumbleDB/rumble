(:JIQS: ShouldRun; UpdateDim=[2,9]; Output="" :)
for $data in delta-file("./tempDeltaTable")
count $c
return
    if ($c eq 1)
    then
        insert {"is_1_2" : true} into $data
    else
        insert {"is_not_1_2" : true} into $data