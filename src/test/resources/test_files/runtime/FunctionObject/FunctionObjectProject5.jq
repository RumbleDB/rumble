(:JIQS: ShouldRun; Output="({ "Captain" : "Kirk", "First Officer" : "Spock" }, { "Captain" : "Han Solo", "First Mate" : "Chewbacca" })" :)
project(({"Captain" : "Kirk", "First Officer" : "Spock", "Engineer" : "Scott"}, {"Captain" : "Han Solo", "First Mate" : "Chewbacca"}), ("Captain", "First Officer", "First Mate"))

(: Multiple objects with mix of existing and non-existing keys :)
