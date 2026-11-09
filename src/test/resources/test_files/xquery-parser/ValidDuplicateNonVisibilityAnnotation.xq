(:JIQS: ShouldRun; Output="84" :)
declare namespace a = "http://example.com/annotations";
declare %a:translucent("true") %a:translucent("false") function local:foo() { 42 };
declare %a:translucent("true") %a:translucent("false") variable $foo := 42;
local:foo() + $foo
