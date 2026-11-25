(:JIQS: ShouldRun; Output="true":)
(: XQuery 3.1 §§2.4.1, 3.9.3.1: empty text disappears before attribute checks;
   standalone nodes retain identity and a consistent implementation-dependent order. :)
let $p := <?p x?>, $q := <?p x?>, $c := <!--c-->, $d := <!--c-->
return every $ok in (
    count(element a {text {""}, namespace p {"urn:p"}, attribute x {"y"}}/@x) eq 1,
    string(element a {text {""}, text {"a"}, text {"b"}}) eq "ab",
    try { string(element a {text {"x"}, attribute a {"y"}}) eq "missing-error" }
        catch err:XQTY0024 { true() },
    try { string(element a {text {""}, comment {"x"}, attribute a {"y"}}) eq "missing-error" }
        catch err:XQTY0024 { true() },
    count(($p, $p, $q) union ()) eq 2,
    count(($c, $c, $d) union ()) eq 2,
    ($p is $p) and not($p is $q),
    ($c is $c) and not($c is $d),
    ($p << $q) eq ($q >> $p),
    ($c << $d) eq ($d >> $c),
    count(element a {$p, $c}/node()) eq 2
) satisfies $ok
