(:JIQS: ShouldRun; Output="({ "Captain" : "Kirk", "First Officer" : "Spock" }, { "Captain" : "Han Solo", "First Mate" : "Chewbacca" })" :)
remove-keys(({"Captain" : "Kirk", "First Officer" : "Spock", "Engineer" : "Scott"}, {"Captain" : "Han Solo", "First Mate" : "Chewbacca"}), ("Engineer", "Wookie"))

(: Multiple objects with mix of existing and non-existing keys :)
