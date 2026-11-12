(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
(: The declared type of a global variable is checked statically: the initializer may return any item, although at runtime it returns a string. :)
declare variable $x as string := parse-json("\"a\"");
$x
