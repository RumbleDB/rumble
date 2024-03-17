(:JIQS: ShouldCompile :)
declare %an:sequential function bar() { 1 };
declare %an:nonsequential function foo() {
    bar();
 };

(: declared nonsequential with sequential body is valid as it is a non-updating apply statement :)