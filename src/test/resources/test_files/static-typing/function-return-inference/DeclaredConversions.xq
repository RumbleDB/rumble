(:JIQS: ShouldRun; Output="(42, 42)" :)
declare function local:value() as xs:integer { <a>42</a> };
(local:value#0)() is statically xs:integer,
(function() as xs:integer { <a>42</a> })() is statically xs:integer
