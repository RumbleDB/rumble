(:JIQS: ShouldRun; UpdateDim=[3,11]; Output="" :)
let $R := delta-file("./R")
let $usersSeq := $R.users[]
let $remove_uid := $usersSeq[$$.name eq "Dee Linquent"].userID
return
    (
    for $item in $R.items[]
    count $c
    where $item.offered_by = $remove_uid
    return delete json $R.items[[$c]]
    ,
    for $bid in $R.bids[]
    count $c
    where $bid.userID = $remove_uid
    return delete json $R.bids[[$c]]
    ,
    for $user in $R.users[]
    count $c
    where $user.name eq "Dee Linquent"
    return delete json $R.users[[$c]]
    )