(:JIQS: ShouldRun; Output="(1, 1, 1)" :)
(: The math functions of one number, or of a constructed element, whose typed value is one untyped value, return
   one xs:double. :)
declare function local:f($x as xs:double) as xs:double { math:cos($x) + math:pow(math:sqrt($x), 2) };
declare function local:g($n as xs:integer) as xs:double { math:exp($n) };
declare function local:h() as xs:double { math:exp(<a>0</a>) };
(local:f(0e0), local:g(0), local:h())
