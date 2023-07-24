(:JIQS: ShouldRun; UpdateDim=[3,1]; Output="" :)
let $users := delta-file("./R").users
return append {"userID" : "U07", "name" : "Annabel Lee"} into $users