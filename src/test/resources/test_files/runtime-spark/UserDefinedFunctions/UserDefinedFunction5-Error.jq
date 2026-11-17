(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function local:increment($values as integer*) as string* {
  parallelize($values) ! ($$ + 1)
};
local:increment((1, 2, 3))

(: As UserDefinedFunction5, but the integers of the result cannot be converted to strings. :)
