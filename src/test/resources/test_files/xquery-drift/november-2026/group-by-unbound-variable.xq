(:JIQS: ShouldCrash; ErrorCode="XQST0094":)
(: A grouping variable without a binding expression must name a variable in the input tuples.
   https://www.w3.org/TR/xquery-31/#id-group-by :)
for $x in (1, 2)
group by $missing
return $x
