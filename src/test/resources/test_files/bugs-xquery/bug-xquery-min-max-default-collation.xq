(:JIQS: ShouldRun; Output="true" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive";
max(("a", "B")) eq "B" and min(("a", "B")) eq "a"
