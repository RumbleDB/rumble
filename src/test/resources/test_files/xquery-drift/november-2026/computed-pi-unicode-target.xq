(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.9.3.5 accepts a target castable to xs:NCName, including é.
   https://www.w3.org/TR/xquery-31/#id-computedPIs
   Observed on master c6cbdb7fa: XQDY0041 instead of true. :)
name(processing-instruction {"é"} {"ok"}) eq "é"
