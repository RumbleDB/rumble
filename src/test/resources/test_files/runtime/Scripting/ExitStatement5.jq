(:JIQS: ShouldRun; Output="({ "storeid" : 1, "state" : "CA" }, { "storeid" : 2, "state" : "MA" }, { "storeid" : 3, "state" : "MA" }, { "storeid" : 4, "state" : "CA" }, { "storeid" : 5, "state" : "NY" }, { "storeid" : 6, "state" : "MI" }, { "storeid" : 7, "state" : "MI" })" :)
declare function foo() {
    variable $i := 3;
    exit returning parallelize((
                     { "storeid" : 1, "state" : "CA" },
                     { "storeid" : 2, "state" : "MA" },
                     { "storeid" : 3, "state" : "MA" },
                     { "storeid" : 4, "state" : "CA" },
                     { "storeid" : 5, "state" : "NY" },
                     { "storeid" : 6, "state" : "MI" },
                     { "storeid" : 7, "state" : "MI" }
                   ));
    $i
};
foo()