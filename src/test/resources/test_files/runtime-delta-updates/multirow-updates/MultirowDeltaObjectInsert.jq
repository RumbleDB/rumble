(:JIQS: ShouldRun; UpdateDim=[2,3]; Output="" :)
for $data in delta-file("./tempDeltaTable")
count $c
return
    if ($c eq 1)
    then
        insert {"is_1" : true} into $data
    else
        insert {"is_not_1" : true} into $data