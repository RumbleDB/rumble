(:JIQS: ShouldRun; UpdateDim=[3,5]; Output="" :)
let $R := delta-file("./R")
let $users := $R.users[]
let $bids := $R.bids[]
let $uid := $users[$$.name eq "Annabel Lee"].userID
let $top_bid := max($bids[$$.itemNO eq 1002].bid)
return append {"userID" : $uid, "itemNO" : 1002, "bid" : $top_bid * 1.1, "bid_date" : "1999-02-01"} into $R.bids