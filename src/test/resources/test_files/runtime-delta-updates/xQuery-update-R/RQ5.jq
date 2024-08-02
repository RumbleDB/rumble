(:JIQS: ShouldRun; UpdateDim=[3,9]; Output="" :)
let $R := delta-file("./R")
let $bids := $R.bids[]
let $users := $R.users[]
let $uid := $users[$$.name eq "Annabel Lee"].userID
let $top_bid := max($bids[$$.itemNO eq 1007].bid)
return
    if ($top_bid * 1.1 le 200)
    then
        append json {"userID" : $uid, "itemNO" : 1007, "bid" : $top_bid * 1.1, "bid_date" : "1999-02-01"} into $R.bids
    else
        ()