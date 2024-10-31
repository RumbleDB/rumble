(:JIQS: ShouldRun; Output="{ "Captain" : "Kirk" }" :)
project({"Captain" : "Kirk", "First Officer" : "Spock", "Engineer" : "Scott"}, ("Captain", "non-existing-key"))

(: Mix of existing and non-existing keys :)
