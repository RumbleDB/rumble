(:JIQS: ShouldCompile :)
declare %an:nonsequential function foo() {
    copy json $je := {"a" : 1}
    modify delete json $je.b
    return $je
 };
