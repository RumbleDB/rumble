(:JIQS: ShouldRun; Output="{ "First Officer" : "Spock", "Engineer" : "Scott" }" :)
remove-keys({"Captain" : "Kirk", "First Officer" : "Spock", "Engineer" : "Scott"}, ("Captain", "non-existing-key"))


(: Mix of existing and non-existing keys :)
