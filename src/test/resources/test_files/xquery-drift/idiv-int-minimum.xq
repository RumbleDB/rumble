(:JIQS: ShouldRun; Output="true":)
(: Observed on master f1775366d: -2147483648 instead of 2147483648. :)
(: https://www.w3.org/TR/xpath-functions-31/#func-numeric-integer-divide :)
(: Arithmetic on xs:int operands must not wrap at the 32-bit boundary. :)
(xs:int("-2147483648") idiv xs:int("-1")) eq 2147483648
