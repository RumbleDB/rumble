(:JIQS: ShouldRun; Output="(aİ, c)" :)
(: Lowercasing İ yields two chars; matched indices must still refer to the original string. :)
substring-before("aİbc", "B", "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind"),
substring-after("aİbc", "B", "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind")
