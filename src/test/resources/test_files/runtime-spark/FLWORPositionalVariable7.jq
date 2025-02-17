(:JIQS: ShouldRun; Output="({ "a" : true, "i" : 10, "x" : 1 }, { "a" : true, "i" : 11, "x" : 2 }, { "a" : true, "i" : 12, "x" : 3 }, { "a" : true, "i" : 13, "x" : 4 }, { "a" : true, "i" : 14, "x" : 5 }, { "a" : true, "i" : 15, "x" : 6 }, { "a" : true, "i" : 16, "x" : 7 }, { "a" : true, "i" : 17, "x" : 8 }, { "a" : true, "i" : 18, "x" : 9 }, { "a" : true, "i" : 19, "x" : 10 }, { "a" : false, "i" : 10, "x" : 1 }, { "a" : false, "i" : 11, "x" : 2 }, { "a" : false, "i" : 12, "x" : 3 }, { "a" : false, "i" : 13, "x" : 4 }, { "a" : false, "i" : 14, "x" : 5 }, { "a" : false, "i" : 15, "x" : 6 }, { "a" : false, "i" : 16, "x" : 7 }, { "a" : false, "i" : 17, "x" : 8 }, { "a" : false, "i" : 18, "x" : 9 }, { "a" : false, "i" : 19, "x" : 10 })" :)

for $a in parallelize((true, false))
for $i at $x in parallelize(10 to 19)
return { "a" : $a, "i" : $i, "x" : $x }
