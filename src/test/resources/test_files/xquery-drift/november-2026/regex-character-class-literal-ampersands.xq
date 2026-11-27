(:JIQS: ShouldRun; Output="true":)
(: Each ampersand is a literal character in the character class.
   https://www.w3.org/TR/xpath-functions-31/#regex-syntax
   https://www.w3.org/TR/xmlschema-2/#nt-charGroup :)
matches("&amp;", "^[a&amp;&amp;b]$")
