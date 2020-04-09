(:JIQS: ShouldRun; Output="2" :)
declare function foo($x)
{
  function() { $x }
};
let $f := foo(2)
return $f()
