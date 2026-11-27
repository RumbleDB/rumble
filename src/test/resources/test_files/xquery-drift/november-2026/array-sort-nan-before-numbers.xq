(:JIQS: ShouldRun; Output="true":)
(: An array sort key of NaN precedes ordinary numeric keys.
   https://www.w3.org/TR/xpath-functions-31/#func-array-sort :)
let $nan := xs:double("NaN")
return deep-equal(array:sort([1, $nan, -1]), [$nan, -1, 1])
