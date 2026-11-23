(:JIQS: ShouldRun; Output="1" :)
(: A cycle through a global variable is not narrowed either. :)
declare variable $v := local:f(0);
declare function local:f($n as xs:integer) { if ($n gt 0) then $v else 1 };
(local:f#1)(0) is statically item()*
