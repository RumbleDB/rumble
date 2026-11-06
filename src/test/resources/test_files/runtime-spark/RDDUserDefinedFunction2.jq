(:JIQS: ShouldRun; Output="(c, b, a)" :)
declare function rdd_function1 (){
    parallelize(({name: "a", index: 1}, {name: "b", index: 2}, {name: "c", index: 3}))
};

declare function rdd_function2 () {
    for $i in rdd_function1()
    order by $i.index descending
    return $i.name
};

rdd_function2()


(: Function using RDD in a FLWOR clause :)
