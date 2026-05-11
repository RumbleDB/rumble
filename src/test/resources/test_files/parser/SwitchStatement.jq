(:JIQS: ShouldParse :)
switch (4)
case 1 + 1 return {parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": 2})[500];}
case 2 + 2 return {parallelize(for $i in 1 to 1000 return ["alice", "bob", "eve"])[500];}
default return {parallelize(for $i in 1 to 1000 return "none")[500];}