(:JIQS: ShouldRun; Output="[ { "foo" : "bar" }, "foobar" ]" :)
(
if((true))
then
parallelize(for $i in 1 to 1000 return [{"foo": "bar"}, "foobar"])
else
parallelize(for $i in 1 to 1000 return {"alice" : {"bob": "eve"}})
)[500]
