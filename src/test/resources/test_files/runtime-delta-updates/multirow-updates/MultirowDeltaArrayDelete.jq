(:JIQS: ShouldRun; UpdateDim=[2,5]; Output="" :)
for $arr in delta-file("./tempDeltaTable").foobar
count $c1
return
    for $val in $arr[]
    count $c2
    where $c1 eq $c2
    return delete $arr[[$c2]]