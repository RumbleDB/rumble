(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
(: A constructed element is not validated, so its typed value is its string value, as one xs:untypedAtomic. :)
data(<a>1</a>) is statically xs:untypedAtomic,
(<a>1</a> + 1) is statically xs:double,
xs:integer(element b { 3 }) is statically xs:integer
