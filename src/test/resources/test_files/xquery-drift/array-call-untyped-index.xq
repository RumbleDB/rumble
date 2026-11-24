(:JIQS: ShouldRun; Output="true":)
(: Observed on master f1775366d: XPTY0004 instead of selecting "x". :)
(: https://www.w3.org/TR/xquery-31/#id-function-conversion-rules :)
(: https://www.w3.org/TR/xquery-31/#id-array-lookup :)
(: Function conversion casts an untyped argument to the integer parameter type. :)
["x"](xs:untypedAtomic("1")) eq "x"
