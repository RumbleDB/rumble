(:JIQS: ShouldRun; Output="true":)
(: A switch compares its operand to a case using the default collation.
   https://www.w3.org/TR/xquery-31/#id-switch :)
declare default collation "http://www.w3.org/2005/xpath-functions/collation/html-ascii-case-insensitive";
switch ("a")
    case "A" return true()
    default return false()
