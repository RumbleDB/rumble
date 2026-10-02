(:JIQS: ShouldRun; Output="true":)
declare base-uri "http://www.w3.org/2005/xpath-functions/";
contains("banana", "ana", "http://www.w3.org/2013/collation/UCA?lang=en")
and substring-before("database", "BASE", "http://www.w3.org/2013/collation/UCA?lang=en;strength=primary") eq "data"
and substring-after("database", "DATA", "http://www.w3.org/2013/collation/UCA?lang=en;strength=primary") eq "base"
and ends-with("database", "BASE", "http://www.w3.org/2013/collation/UCA?lang=en;strength=primary")
and substring-after("banana", "a", "collation/codepoint") eq "nana"
and contains("", "-", "http://www.w3.org/2013/collation/UCA?alternate=blanked")
and starts-with("", "-", "http://www.w3.org/2013/collation/UCA?alternate=blanked")
and ends-with("", "-", "http://www.w3.org/2013/collation/UCA?alternate=blanked")
and substring-before("database", "-", "http://www.w3.org/2013/collation/UCA?alternate=blanked") eq ""
and substring-after("database", "-", "http://www.w3.org/2013/collation/UCA?alternate=blanked") eq "database"

