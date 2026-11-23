(:JIQS: ShouldRun; Output="true":)
(: https://www.w3.org/TR/xpath-functions-31/#func-round :)
(: https://www.w3.org/TR/xpath-functions-31/#func-round-half-to-even :)
every $round in (round#2, round-half-to-even#2)
satisfies
  $round(xs:int(-2147483648), -1) eq -2147483650
  and $round(xs:int(2147483647), 0) eq 2147483647
  and $round(xs:int(2147483647), -20) eq 0
  and $round(xs:long(9223372036854775807), -1) eq 9223372036854775810
