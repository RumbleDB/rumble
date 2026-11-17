(:JIQS: ShouldRun; Output="true" :)
declare base-uri "http://www.w3.org/2005/xpath-functions/";
compare("abc", "ABC", "collation/html-ascii-case-insensitive") eq 0
and string-join(sort(("b", "a"), ()), ",") eq "a,b"
and string-join(array:flatten(array:sort(["b", "a"], ())), ",") eq "a,b"
and count(distinct-values(("a", "A", "b"), "collation/html-ascii-case-insensitive")) eq 2
