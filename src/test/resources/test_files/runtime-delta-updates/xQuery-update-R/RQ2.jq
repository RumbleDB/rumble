(:JIQS: ShouldRun; UpdateDim=[3,3]; Output="" :)
let $bids := delta-file("./R").bids
return append {"userID" : "U07", "itemNO" : 1001, "bid" : 60, "bid_date" : "1999-02-01"} into $bids