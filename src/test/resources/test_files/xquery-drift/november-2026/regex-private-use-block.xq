(:JIQS: ShouldRun; Output="true":)
(: XML Schema regular expressions recognize the PrivateUse block.
   https://www.w3.org/TR/xpath-functions-31/#regex-syntax
   https://www.w3.org/TR/xmlschema-2/#nt-IsBlock :)
matches("&#xE000;", "^\p{IsPrivateUse}$")
