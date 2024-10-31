(:JIQS: ShouldRun; Output="(, )" :)
replace("", ".+", "*"),
replace((), ".+", "*")

(: the empty sequence and the zero-length string result in the zero-length string :)
