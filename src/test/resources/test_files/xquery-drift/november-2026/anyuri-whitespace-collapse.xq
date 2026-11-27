(:JIQS: ShouldRun; Output="true":)
(: Casting to xs:anyURI applies the whitespace collapse facet, including within the string.
   https://www.w3.org/TR/xpath-functions-31/#casting-from-string
   https://www.w3.org/TR/xmlschema11-2/#anyURI :)
string(xs:anyURI("  a&#x9;  b  ")) eq "a b"
