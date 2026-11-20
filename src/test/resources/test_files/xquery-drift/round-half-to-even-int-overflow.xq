(:JIQS: ShouldRun; Output="2147483650":)
(: https://www.w3.org/TR/xpath-functions-31/#func-round-half-to-even :)
(: Rounding an xs:int may produce a result outside the xs:int range. :)
round-half-to-even(xs:int(2147483647), -1)
