(:JIQS: ShouldRun; Output="([ "alice", "bob", "eve" ], none, { "foo" : 1, "bar" : 2 }, { "bar" : "bar" }, { "bar" : "bar" })" :)
switch (4)
case 1 + 1 return parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500]
case 2 + 2 return parallelize(for $i in 1 to 1000 return ["alice", "bob", "eve"])[500]
default return parallelize(for $i in 1 to 1000 return "none")[500],
switch (())
case 1 + 1 return parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500]
case 2 + 2 return parallelize(for $i in 1 to 1000 return ["alice", "bob", "eve"])[500]
default return parallelize(for $i in 1 to 1000 return "none")[500],
switch (true)
case 1 + 1 eq 2 return parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500]
default return parallelize(for $i in 1 to 1000 return "none")[500],
switch ("bar")
case "foo" return parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500]
case "foobar" return parallelize(for $i in 1 to 1000 return {"foobar": "bar"})[500]
case "bar" return parallelize(for $i in 1 to 1000 return {"bar": "bar"})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500],
switch ("bar")
case "foo" case "barfoo" return parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500]
case "foobar" case "bar" return parallelize(for $i in 1 to 1000 return {"bar": "bar"})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": "bar"})[500]
