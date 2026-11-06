(:JIQS: ShouldRun; Output="({ "bar" : [ 1 ], "foo" : [ ], "pos" : 0 }, { "bar" : [ 2 ], "foo" : [ ], "pos" : 0 })" :)
variable $res := ();
for $bar in parallelize((1, 2))
for $foo allowing empty at $pos in parallelize(())
return $res := ($res, { "bar" : [ $bar ], "foo" : [ $foo ], "pos" : $pos });
$res