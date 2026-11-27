(:JIQS: ShouldRun; Output="1":)
(: The element supplies one atomic value and the array supplies none.
   https://www.w3.org/TR/xquery-31/#id-cast :)
(<a>1</a>, [()]) cast as xs:integer
