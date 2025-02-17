(:JIQS: ShouldRun; Output="({ "bar" : [ [ 1, 2 ], 1, 2, 3 ] }, { "bar" : "default" }, { "bar" : "default" }, { "bar" : "default" }, { "bar" : "default" })" :)
typeswitch([1,2])
case string return parallelize(for $i in 1 to 1000 return {"bar": "string"})[500]
case $a as boolean+ | array* return parallelize(for $i in 1 to 1000 return {"bar": ($a, 1, 2, 3)})[500]
default return parallelize(for $i in 1 to 1000 return {"bar": "default"})[500],
typeswitch([1,2])
case string return parallelize(for $i in 1 to 1000 return {"bar": "string"})[500]
case $a as boolean+ | object* return parallelize(for $i in 1 to 1000 return {"bar": ($a, 1, 2, 3)})[500]
default return parallelize(for $i in 1 to 1000 return {"bar": "default"})[position() ge 500 and position() le 503]

(: case with arrays and variables :)
