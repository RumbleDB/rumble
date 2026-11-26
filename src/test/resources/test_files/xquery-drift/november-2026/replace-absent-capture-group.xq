(:JIQS: ShouldRun; Output="true":)
(: A single-digit reference beyond the last capture group expands to the empty string.
   https://www.w3.org/TR/xpath-functions-31/#func-replace :)
replace("abc", "b", "$3") eq "ac"
