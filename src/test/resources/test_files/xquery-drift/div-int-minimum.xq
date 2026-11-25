(:JIQS: ShouldRun; Output="true":)
(: Observed on master f1775366d: Java ArithmeticException (Overflow) instead of 2147483648. :)
(: https://www.w3.org/TR/xpath-functions-31/#func-numeric-divide :)
(: The decimal quotient is representable even though it exceeds xs:int. :)
(xs:int("-2147483648") div xs:int("-1")) eq 2147483648
