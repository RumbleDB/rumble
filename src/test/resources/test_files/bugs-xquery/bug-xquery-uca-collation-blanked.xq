(:JIQS: ShouldRun; Output="(0, true, true)" :)
(: Under alternate=blanked, variable characters such as spaces and hyphens have no collation units. :)
compare("database", "data base", "http://www.w3.org/2013/collation/UCA?lang=en;alternate=blanked;strength=quaternary"),
starts-with("-abcdefghi", "-abc", "http://www.w3.org/2013/collation/UCA?lang=en;alternate=blanked;strength=primary"),
ends-with("xabc-", "abc", "http://www.w3.org/2013/collation/UCA?lang=en;alternate=blanked;strength=primary")
