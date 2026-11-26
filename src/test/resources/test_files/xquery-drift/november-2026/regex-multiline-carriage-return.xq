(:JIQS: ShouldRun; Output="true":)
(: A carriage return does not start a new line for the multiline start anchor.
   https://www.w3.org/TR/xpath-functions-31/#regex-start-end :)
not(matches("x&#xD;y", "^y", "m"))
