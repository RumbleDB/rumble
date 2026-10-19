(:JIQS: ShouldRun; Output="(-1, -1, -1, 1, 1)" :)
(: With the default fallback=yes, unsupported UCA parameter values are ignored. :)
compare("abc", "def", "http://www.w3.org/2013/collation/UCA?strength=unknown"),
compare("abc", "def", "http://www.w3.org/2013/collation/UCA?alternate=unknown"),
compare("abc", "def", "http://www.w3.org/2013/collation/UCA?unknownKeyword=yes"),
compare("database", "Database", "http://www.w3.org/2013/collation/UCA?lang=en;strength=tertiary;caseFirst=upper"),
compare("database", "Database", "http://www.w3.org/2013/collation/UCA?lang=en;fallback=no;caseFirst=upper")
