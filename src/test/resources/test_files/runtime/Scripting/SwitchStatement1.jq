(:JIQS: ShouldRun; Output="{ "1" : [ "alice", "bob", "eve" ], "2" : "none", "3" : { "foo" : 1, "bar" : 2 }, "4" : { "bar" : "bar" }, "5" : { "bar" : "bar" } }" :)
variable $res := {};
switch (4)
case 1 + 1 return insert json "1": parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500] into $res;
case 2 + 2 return insert json "1": parallelize(for $i in 1 to 1000 return ["alice", "bob", "eve"])[500] into $res;
default return insert json "1": parallelize(for $i in 1 to 1000 return "none")[500] into $res;
switch (())
case 1 + 1 return insert json "2": parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500] into $res;
case 2 + 2 return insert json "2": parallelize(for $i in 1 to 1000 return ["alice", "bob", "eve"])[500] into $res;
default return insert json "2": parallelize(for $i in 1 to 1000 return "none")[500] into $res;
switch (true)
case 1 + 1 eq 2 return insert json "3": parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500] into $res;
default return insert json "3": parallelize(for $i in 1 to 1000 return "none")[500] into $res;
switch ("bar")
case "foo" return insert json "4": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;
case "foobar" return insert json "4": parallelize(for $i in 1 to 1000 return {"foobar": "bar"})[500] into $res;
case "bar" return insert json "4": parallelize(for $i in 1 to 1000 return {"bar": "bar"})[500] into $res;
default return insert json "4": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;
switch ("bar")
case "foo" case "barfoo" return insert json "5": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;
case "foobar" case "bar" return insert json "5": parallelize(for $i in 1 to 1000 return {"bar": "bar"})[500] into $res;
default return insert json "5": parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500] into $res;
$res