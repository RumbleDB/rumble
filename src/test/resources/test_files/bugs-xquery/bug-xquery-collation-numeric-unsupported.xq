(:JIQS: ShouldCrash; ErrorCode="FOCH0004":)
(: ICU StringSearch rejects numeric collations, which have no collation units. :)
substring-before("Chapter-100", "Chapter-10", "http://www.w3.org/2013/collation/UCA?lang=en;numeric=yes")
