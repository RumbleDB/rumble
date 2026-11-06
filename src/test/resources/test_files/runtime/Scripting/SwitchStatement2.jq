(:JIQS: ShouldRun; Output="{ "1" : [ 1, 2, 3 ] }" :)
variable $res := {"1": [1,2,3]};
switch ("bar")
case "foo" return {variable $res := {}; insert json "4": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;}
case "foobar" return {variable $res := {}; insert json "4": parallelize(for $i in 1 to 1000 return {"foobar": "bar"})[500] into $res;}
case "bar" return {variable $res := {}; insert json "4": parallelize(for $i in 1 to 1000 return {"bar": "bar"})[500] into $res;}
default return {variable $res := {}; insert json "4": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;}
$res