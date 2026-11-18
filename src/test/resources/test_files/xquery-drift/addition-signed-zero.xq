(:JIQS: ShouldRun; Output="true":)
(: https://www.w3.org/TR/xpath-functions-31/#op.numeric :)
(: Positive zero plus negative zero must produce positive zero. :)
(1 div (0 + (-0.0e0))) eq xs:double("INF")
