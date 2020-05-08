(:JIQS: ShouldRun; Output="(1, 2, 3)" :)
declare function rdd_function (){
    parallelize((1,2,3))
};

rdd_function()

(: Function returning an RDD :)
