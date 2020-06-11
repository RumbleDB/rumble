(:JIQS: ShouldRun; Output="({ "_c0" : "LatD", "_c1" : "LatM", "_c2" : "LatS", "_c3" : "NS", "_c4" : "LonD", "_c5" : "LonM", "_c6" : "LonS", "_c7" : "EW", "_c8" : "City", "_c9" : "State" }, { "_c0" : "41", "_c1" : "5", "_c2" : "59", "_c3" : "N", "_c4" : "80", "_c5" : "39", "_c6" : "0", "_c7" : "W", "_c8" : "Youngstown", "_c9" : "OH" })" :)
for $i in csv-file("../../../queries/cities.csv")
count $c
where $c le 2
return $i
