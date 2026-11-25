(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §3.9.3.7 accepts a prefix castable to xs:NCName, including é.
   https://www.w3.org/TR/xquery-31/#id-computedNamespaces
   Observed on master c6cbdb7fa: XQDY0074 instead of true. :)
name(namespace {"é"} {"urn:test"}) eq "é"
