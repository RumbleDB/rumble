(:JIQS: ShouldRun; Output="true":)
(: The dot wildcard matches U+0085 even when dot-all mode is off.
   https://www.w3.org/TR/xpath-functions-31/#flags :)
matches("&#x85;", ".")
