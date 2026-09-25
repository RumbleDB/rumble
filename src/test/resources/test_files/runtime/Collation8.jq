(:JIQS: ShouldRun; Output="Frog" :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/codepoint";

for $i in ("Frog", "frog")
group by $g := $i collation "http://www.w3.org/2010/09/qt-fots-catalog/collation/caseblind"
return $g
