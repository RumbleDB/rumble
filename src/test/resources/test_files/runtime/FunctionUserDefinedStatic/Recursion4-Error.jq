(:JIQS: ShouldCrash; ErrorCode="XPTY0004" :)
declare function local:sum($n as integer, $total as integer) as string {
  if ($n eq 0) then $total else local:sum($n - 1, $total + $n)
};

local:sum(1000, 0)

(: As Recursion4, but the integer result of the last tail call cannot be converted to a string. :)
