(:JIQS: ShouldCompile :)
declare %nonsequential function foo() {
    copy $je := {"a" : 1}
    modify delete json $je.b
    return $je
 };
