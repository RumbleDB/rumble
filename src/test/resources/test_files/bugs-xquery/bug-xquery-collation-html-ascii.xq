(:JIQS: ShouldRun; Output="true":)
contains("iNPut", "PU", "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive")
and not(contains("hôtel", "HÔT", "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive"))
