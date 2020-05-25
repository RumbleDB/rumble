(:JIQS: ShouldRun; Output="(string, integer, object)" :)
for $o in parallelize(("foo", 1, {}))
return typeswitch($o)
case integer return "integer"
case string return "string"
case object return "object"
default return "other"
