(:JIQS: ShouldRun; Output="(true, 2)" :)
(: In construction mode preserve, a constructed element is annotated xs:anyType, and its typed value is the same. :)
declare construction preserve;
<a/> is statically element(a, xs:anyType) instance of element(a, xs:anyType),
(<a>1</a> + 1) is statically xs:double
