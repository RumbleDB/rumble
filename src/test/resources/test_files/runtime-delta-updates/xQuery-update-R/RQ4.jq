(:JIQS: ShouldRun; UpdateDim=[3,7]; Output="" :)
for $user in delta-file("./R").users[]
where $user.name eq "Annabel Lee"
return
    if ($user.rating)
    then
        replace value of json $user.rating with "B"
    else
        insert json "rating" : "B" into $user