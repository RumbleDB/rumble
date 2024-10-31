(:JIQS: ShouldRun; Output="({ "foo" : "0123456789ABCDEF" }, { "foo" : "4234.243 correct" }, { "foo" : "AaBb" }, { "foo" : false }, { "foo" : true })" :)
typeswitch(hexBinary("0123456789abcdef"))
case $a as base64Binary? return parallelize(for $i in 1 to 1000 return {"foo": "no"})[500]
case $b as boolean+ | double return parallelize(for $i in 1 to 1000 return {"foo": ($b cast as string) || "no"})[500] 
case $a as string | decimal+ | hexBinary* return parallelize(for $i in 1 to 1000 return {"foo": $a})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": "wrong"})[500],
typeswitch(4.234243e3)
case $a as hexBinary+ return parallelize(for $i in 1 to 1000 return {"foo": "no"})[500]
case $b as boolean | double? | base64Binary? return parallelize(for $i in 1 to 1000 return {"foo": ($b cast as string) || " correct"})[500]
case $a as string | decimal? return parallelize(for $i in 1 to 1000 return {"foo": $a})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": "default"})[500],
typeswitch(base64Binary("AaBb"))
case $a as hexBinary return parallelize(for $i in 1 to 1000 return {"foo": "no"})[500]
case $b as boolean? | double+ return parallelize(for $i in 1 to 1000 return {"foo": ($b cast as string) || "no"})[500]
case $a as string | decimal? return parallelize(for $i in 1 to 1000 return {"foo": $a})[500]
default $def return parallelize(for $i in 1 to 1000 return {"foo": $def})[500],
typeswitch(false)
case $a as integer+ | double | decimal* return parallelize(for $i in 1 to 1000 return {"foo": $a * 5})[500]
case string? return parallelize(for $i in 1 to 1000 return {"foo": "no"})[500]
case $a as boolean return parallelize(for $i in 1 to 1000 return {"foo": $a})[500]
default return parallelize(for $i in 1 to 1000 return {"foo": duration("P3Y")})[500],
typeswitch(duration("P4Y5MT5M"))
case $a as string | decimal+ | hexBinary? return parallelize(for $i in 1 to 1000 return {"foo": $a})[500]
case $a as base64Binary | duration* return parallelize(for $i in 1 to 1000 return {"foo": ends-with(($a cast as string), "5M")})[500]
default $a return parallelize(for $i in 1 to 1000 return {"foo": "default"})[500]

(: case with binary, numeric, duration and variables :)
