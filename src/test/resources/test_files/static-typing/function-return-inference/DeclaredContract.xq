(:JIQS: ShouldRun; Output="(42, 42)" :)
declare function local:answer() as item()* { 42 };
(local:answer#0)() is statically item()*,
(function() as item()* { 42 })() is statically item()*
