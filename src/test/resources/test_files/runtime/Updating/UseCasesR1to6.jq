(:JIQS: ShouldRun; Output="(USERS:, { "userID" : "U01", "name" : "Tom Jones", "rating" : "B" }, { "userID" : "U02", "name" : "Mary Doe", "rating" : "A" }, { "userID" : "U04", "name" : "Roger Smith", "rating" : "C" }, { "userID" : "U05", "name" : "Jack Sprat", "rating" : "B" }, { "userID" : "U06", "name" : "Rip Van Winkle", "rating" : "B" }, { "userID" : "U07", "name" : "Annabel Lee", "rating" : "B" }, ITEMS:, { "itemNO" : "1001", "description" : "Red Bicycle", "offered_by" : "U01", "start_date" : "1999-01-05", "end_date" : "1999-01-20", "reserve_price" : 40 }, { "itemNO" : "1002", "description" : "Motorcycle", "offered_by" : "U02", "start_date" : "1999-02-11", "end_date" : "1999-03-15", "reserve_price" : 500 }, { "itemNO" : "1003", "description" : "Old Bicycle", "offered_by" : "U02", "start_date" : "1999-01-10", "end_date" : "1999-02-20", "reserve_price" : 25 }, { "itemNO" : "1004", "description" : "Tricycle", "offered_by" : "U01", "start_date" : "1999-02-25", "end_date" : "1999-03-08", "reserve_price" : 15 }, { "itemNO" : "1007", "description" : "Racing Bicycle", "offered_by" : "U04", "start_date" : "1999-01-20", "end_date" : "1999-02-20", "reserve_price" : 200 }, { "itemNO" : "1008", "description" : "Broken Bicycle", "offered_by" : "U01", "start_date" : "1999-02-05", "end_date" : "1999-03-06", "reserve_price" : 25 }, BIDS:, { "userID" : "U02", "itemNO" : 1001, "bid" : 35, "bid_date" : "1999-01-07" }, { "userID" : "U04", "itemNO" : 1001, "bid" : 40, "bid_date" : "1999-01-08" }, { "userID" : "U02", "itemNO" : 1001, "bid" : 45, "bid_date" : "1999-01-11" }, { "userID" : "U04", "itemNO" : 1001, "bid" : 50, "bid_date" : "1999-01-13" }, { "userID" : "U02", "itemNO" : 1001, "bid" : 55, "bid_date" : "1999-01-15" }, { "userID" : "U01", "itemNO" : 1002, "bid" : 400, "bid_date" : "1999-02-14" }, { "userID" : "U02", "itemNO" : 1002, "bid" : 600, "bid_date" : "1999-02-16" }, { "userID" : "U04", "itemNO" : 1002, "bid" : 1000, "bid_date" : "1999-02-25" }, { "userID" : "U02", "itemNO" : 1002, "bid" : 1200, "bid_date" : "1999-03-02" }, { "userID" : "U04", "itemNO" : 1003, "bid" : 15, "bid_date" : "1999-01-22" }, { "userID" : "U05", "itemNO" : 1003, "bid" : 20, "bid_date" : "1999-02-03" }, { "userID" : "U01", "itemNO" : 1004, "bid" : 40, "bid_date" : "1999-03-05" }, { "userID" : "U05", "itemNO" : 1007, "bid" : 200, "bid_date" : "1999-02-08" }, { "userID" : "U04", "itemNO" : 1007, "bid" : 225, "bid_date" : "1999-02-12" }, { "userID" : "U07", "itemNO" : 1001, "bid" : 60, "bid_date" : "1999-02-01" }, { "userID" : "U07", "itemNO" : 1002, "bid" : 1320, "bid_date" : "1999-02-01" })" :)
let $users := json-lines("./datasets/users.json")
let $items := json-lines("./datasets/items.json")
let $bids := json-lines("./datasets/bids.json")

let $q1_users := copy $copy_users := [$users]
                 modify append json {"userID" : "U07", "name" : "Annabel Lee"} into $copy_users
                 return $copy_users[]

let $q2_bids := copy $copy_bids := [$bids]
                modify append json {"userID" : "U07", "itemNO" : 1001, "bid" : 60, "bid_date" : "1999-02-01"} into $copy_bids
                return $copy_bids[]

let $q3_bids := copy $copy_bids := [$q2_bids], $uid := $q1_users[$$.name eq "Annabel Lee"].userID, $top_bid := max($q2_bids[$$.itemNO eq 1002].bid)
                modify append json {"userID" : $uid, "itemNO" : 1002, "bid" : $top_bid * 1.1, "bid_date" : "1999-02-01"} into $copy_bids
                return $copy_bids[]

let $q4_users := copy $copy_users := [$q1_users]
                modify
                    for $user in $copy_users[]
                    where $user.name eq "Annabel Lee"
                    return
                        if ($user.rating)
                        then
                            replace value of json $user.rating with "B"
                        else
                            insert json "rating" : "B" into $user
                return $copy_users[]

let $q5_bids := copy $copy_bids := [$q3_bids], $uid := $q4_users[$$.name eq "Annabel Lee"].userID, $top_bid := max($q3_bids[$$.itemNO eq 1007].bid)
                modify
                    if ($top_bid * 1.1 le 200)
                    then
                        append json {"userID" : $uid, "itemNO" : 1007, "bid" : $top_bid * 1.1, "bid_date" : "1999-02-01"} into $copy_bids
                    else
                        ()
                return $copy_bids[]

let $to_copy_remove_uid := $q4_users[$$.name eq "Dee Linquent"].userID
let $q6_items := copy $copy_items := [$items], $remove_uid := $to_copy_remove_uid
                 modify
                    for $i in (1 to size($copy_items))
                    return
                        if ($copy_items[[$i]].offered_by eq $remove_uid)
                        then
                            delete json $copy_items[[$i]]
                        else
                            ()
                 return $copy_items[]
 let $q6_bids := copy $copy_bids := [$q5_bids], $remove_uid := $to_copy_remove_uid
                  modify
                     for $i in (1 to size($copy_bids))
                     return
                         if ($copy_bids[[$i]].userID eq $remove_uid)
                         then
                             delete json $copy_bids[[$i]]
                         else
                             ()
                  return $copy_bids[]
let $q6_users := copy $copy_users := [$q4_users]
                 modify
                    for $i in (1 to size($copy_users))
                    return
                        if ($copy_users[[$i]].name eq "Dee Linquent")
                        then
                            delete json $copy_users[[$i]]
                        else
                            ()
                 return $copy_users[]


return ("USERS:", $q6_users, "ITEMS:" , $q6_items, "BIDS:", $q6_bids)