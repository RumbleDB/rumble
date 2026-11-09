(:JIQS: ShouldRun; Output="({ "foo" : "string", "bar" : "string" }, { "foo" : { "bar" : "correct" } }, { "foo" : { "bar" : "AaBb" } }, { "foo" : { "bar" : [ 1, 2 ] } }, { "foo" : { "bar" : { "hel" : "lo" } } })" :)
typeswitch("Hello world!")
case hexBinary? return parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": hexBinary("0123")})[500]
case boolean+ return parallelize(for $i in 1 to 1000 return {"foo": 1, "bar": true})[500]
case decimal return parallelize(for $i in 1 to 1000 return {"foo": 2, "bar": 1})[500]
case string* return parallelize(for $i in 1 to 1000 return {"foo": "string", "bar": "string"})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": "string", "bar": "default"})[500],
typeswitch(duration("P4Y5MT5M"))
case string | decimal? | hexBinary* return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "no"}})[500]
case base64Binary+ return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "no again"}})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "correct"}})[500],
typeswitch(base64Binary("AaBb"))
case $a as hexBinary+ return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "no"}})[500]
case $b as boolean | double+ return parallelize(for $i in 1 to 1000 return {"foo": {"bar": $b}})[500]
case $a as string? | decimal return parallelize(for $i in 1 to 1000 return {"foo": {"bar": $a}})[500]
default $def return parallelize(for $i in 1 to 1000 return {"foo": {"bar": $def}})[500],
typeswitch([1,2])
case string return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "no"}})[500]
case $a as boolean+ | array* return parallelize(for $i in 1 to 1000 return {"foo": {"bar": $a}})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "default"}})[500],
typeswitch({"hel" : "lo"})
case string | boolean? return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "no"}})[500]
case $a as object? | decimal return parallelize(for $i in 1 to 1000 return {"foo": {"bar": $a}})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": {"bar": "default"}})[500]

(: case with string, binary, duration, arrays, objects and variables :)
