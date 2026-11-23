(:JIQS: ShouldRun; Output="(a, b)" :)
(: Inlining local:name copies its body, path included, so the copy does not share the parameter $x. :)
declare function local:name($x as xs:string) { parse-xml($x)/*/local-name() };
(local:name("<a/>"), local:name("<b/>"))
