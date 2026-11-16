(:JIQS: ShouldCrash; ErrorCode="XPTY0004":)
(: position() is an integer, but differs for each item, so the filter keeps all three, which xs:integer? does not accept. :)
declare function local:f($x as xs:integer?) { $x };
local:f((1, 2, 3)[position()])
